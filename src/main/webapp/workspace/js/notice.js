// 공지 불러오기
(async () => {
    const noticeGrid = document.getElementById('noticeGrid');

    try {
        const res = await fetch(`/mock/notice.json`);
        const notices = await res.json();

        noticeGrid.innerHTML = notices.map(n => `
        <div class="notice-card">
          <strong>${n.title}</strong>
          <p>${n.content}</p>
        </div>
      `).join('');
    } catch (err) {
        console.error('❌ 공지 불러오기 실패:', err);
        noticeGrid.innerHTML = '<p>공지사항을 불러올 수 없습니다.</p>';
    }
})();

// todo 공지 등록 버튼 (리더에게만 보이게)
document.querySelector('.register-btn')?.addEventListener('click', () => {
    alert('공지 등록 모달이 열릴 예정입니다.');
});

//todo
// 검색
// 정렬 버튼
document.querySelector('.sort-btn')?.addEventListener('click', () => {
    alert('정렬 기능 실행');
});