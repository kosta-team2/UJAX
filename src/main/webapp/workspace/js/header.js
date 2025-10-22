document.addEventListener('DOMContentLoaded', () => {
    const logoutBtn = document.querySelector('.logout-btn');
    const modeToggle = document.getElementById('modeToggle');
    const themeLink = document.getElementById('theme-style');

    // 다크모드 토글
    modeToggle.addEventListener('click', () => {
        const isLight = themeLink.getAttribute('href').includes('lightmode.css');

        if (isLight) {
            themeLink.href = '../common/css/darkmode.css';
            modeToggle.textContent = '☀️ Light';
        } else {
            themeLink.href = '../common/css/lightmode.css';
            modeToggle.textContent = '🌙 Dark';
        }
        // 로그아웃 버튼 클릭 시
        logoutBtn.addEventListener('click', (e) => {
            e.preventDefault();

            // workspace/ → auth/ 상대 경로로 안전 이동
            window.location.href = '../auth/login.jsp';

        });

        // 로그아웃 버튼
        if (logoutBtn) {
            logoutBtn.addEventListener('click', (e) => {
                e.preventDefault();
                alert('로그아웃 기능이 여기에 연결될 예정입니다.');
            });
        }
    });
})