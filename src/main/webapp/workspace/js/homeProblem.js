// 홈 화면에서 최신 문제 4개만 표시하는 스크립트
window.initHomeProblem = async function () {
    const grid = document.getElementById('homeProblemGrid');
    if (!grid) return;

    try {
        const res = await fetch('/mock/problem.json');
        const problems = await res.json();

        // 최신 4개만 추출
        const recent = problems.slice(0, 4);

        // 카드 렌더링
        grid.innerHTML = recent.map(p => {
            const isSubmitted = p.status ? '제출완료' : '미제출';
            const statusClass = p.status ? 'submitted' : 'not-submitted';

            return `
      <div class="problem-card" data-id="${p.id}">
        <div class="card-top">
          <strong class="problem-title">${p.title}</strong>
          <div class="card-meta">
            <div class="status-badge ${statusClass}">${isSubmitted}</div>
            <span class="difficulty-level">${p.difficulty}</span>
          </div>
        </div>

        <div class="tags">
          ${p.tags.map(tag => `<span>${tag}</span>`).join('')}
        </div>

        <div class="card-bottom">
          <div class="bottom-left">
            <div class="deadline">
              <i class="fa-regular fa-calendar"></i> 마감: ${p.deadline}
            </div>
            <div class="submit-count">
              <i class="fa-solid fa-user-group"></i> 제출자 ${p.submitCount}명
            </div>
          </div>
          <!-- ✅ 카드 전체 클릭이 아닌 버튼만 동작 -->
          <button class="go-btn" data-id="${p.id}">문제 풀기</button>
        </div>
      </div>
      `;
        }).join('');

        // 난이도 색상 클래스 추가
        grid.querySelectorAll('.difficulty-level').forEach((el) => {
            const firstChar = el.textContent.trim().charAt(0);
            if (firstChar === 'G') el.classList.add('gold');
            else if (firstChar === 'S') el.classList.add('silver');
            else if (firstChar === 'B') el.classList.add('bronze');
        });

        // ✅ ‘문제 풀기’ 버튼 클릭 이벤트
        grid.addEventListener('click', (e) => {
            const btn = e.target.closest('.go-btn');
            if (!btn) return; // 버튼 외 클릭 무시
            const id = btn.dataset.id;
            console.log(`[homeProblem] 문제 ${id} 이동`);
            // 문제 페이지 이동
            window.location.href = `problem.html?id=${id}`;
        });

    } catch (err) {
        console.error('❌ 홈 문제 불러오기 실패:', err);
        grid.innerHTML = '<p>문제 데이터를 불러올 수 없습니다.</p>';
    }
};
