document.addEventListener('DOMContentLoaded', () => {
    const logoutBtn = document.querySelector('.logout-btn');
    if (!logoutBtn) return;

    // 로그아웃 버튼 클릭 시
    logoutBtn.addEventListener('click', (e) => {
        e.preventDefault();

		// workspace/ → auth/ 상대 경로로 안전 이동
	    window.location.href = '../auth/login.jsp';
		
    });
});