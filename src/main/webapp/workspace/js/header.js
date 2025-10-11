document.addEventListener('DOMContentLoaded', () => {
    const logoutBtn = document.querySelector('.logout-btn');
    if (!logoutBtn) return;

    // 로그아웃 버튼 클릭 시
    logoutBtn.addEventListener('click', (e) => {
        e.preventDefault();

        alert('로그아웃 기능이 여기에 연결될 예정입니다.');

    });
});