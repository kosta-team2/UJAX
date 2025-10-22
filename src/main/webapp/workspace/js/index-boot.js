document.addEventListener('DOMContentLoaded', () => {
    const frame = document.getElementById('mainFrame');
    const list = document.querySelector('.workspace-list');
    if (!frame || !list) return;

    const firstToggle = list.querySelector('.workspace-toggle');
    if (!firstToggle) {
        return;
    }

    const nameBtn = firstToggle.querySelector('.workspace-name');
    const menu = firstToggle.querySelector('.workspace-menu');
    const homeLink = menu && menu.querySelector('a.nav-btn[href*="home.jsp"]');

    if (menu) menu.classList.add('open');
    if (nameBtn) nameBtn.classList.add('active');

    // todo create.jsp 버튼 생성 후 다시 활성화 할 예정
    // if (homeLink) {
    //     homeLink.classList.add('active');
    //     frame.src = homeLink.href;
    // }
});