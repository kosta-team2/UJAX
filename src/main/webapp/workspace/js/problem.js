// 문제 불러오기
window.initProblem = async function () {
    const problemGrid = document.getElementById('problemGrid');

    try {
        const res = await fetch('/mock/problem.json');
        const problems = await res.json();

        problemGrid.innerHTML = problems.map(p => {
            const isSubmitted = p.status ? '제출완료' : '미제출';
            const statusClass = isSubmitted ? 'submitted' : 'not-submitted';

            return `
        <div class="problem-card">
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
            <button class="go-btn" onclick="location.href='problem.html?id=${p.id}'">이동하기</button>
          </div>
        </div>
      `;
        }).join('');

    } catch (err) {
        problemGrid.innerHTML = '<p>문제 목록을 불러오는 중 오류가 발생했습니다.</p>';
        console.error(err);
    }

    // todo 난이도별 색 변경
    document.querySelectorAll('.difficulty-level').forEach((el) => {
        const firstChar = el.textContent.trim().charAt(0);
        if (firstChar === 'G') el.classList.add('gold');
        else if (firstChar === 'S') el.classList.add('silver');
        else if (firstChar === 'B') el.classList.add('bronze');
    });

    // 문제 등록 버튼
    document.querySelector('.problem-register-btn')?.addEventListener('click', async () => {
        const main = document.getElementById('mainContent');
        try {
            const res = await fetch('problem-register.jsp');
            if (!res.ok) throw new Error(`HTTP ${res.status}`);
            const html = await res.text();
            main.innerHTML = html;

            // registerProblemMount 자동 실행 (기존 JS 사용)
            const root = main.querySelector('#register-problem-fragment');
            if (root && window.registerProblemMount) {
                window.registerProblemMount(root);
            }

        } catch (e) {
            console.error('❌ 문제 등록 페이지 로드 실패:', e);
            main.innerHTML = '<p style="color:red;">문제 등록 페이지를 불러올 수 없습니다.</p>';
        }
    });

//todo
// 검색
// 정렬 버튼
    document.querySelector('.sort-btn')?.addEventListener('click', () => {
        alert('정렬 기능 실행');
    });

};
