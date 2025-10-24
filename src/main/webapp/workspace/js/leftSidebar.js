document.addEventListener('DOMContentLoaded', () => {
    const root = document.querySelector('.left-sidebar');
    if (!root) return;

    // 열린 워크스페이스 토글 모두 닫기
    const closeAll = () => {
        root.querySelectorAll('.workspace-menu.open').forEach(m => m.classList.remove('open'));
        root.querySelectorAll('.workspace-name.active').forEach(b => b.classList.remove('active'));
        root.querySelectorAll('a.nav-btn.active').forEach(a => a.classList.remove('active'));
    };

    root.addEventListener('click', (e) => {
        const btn = e.target.closest('.workspace-name');
        if (!btn || !root.contains(btn)) return;

        const wrap = btn.closest('.workspace-toggle');
        const menu = wrap && wrap.querySelector('.workspace-menu');
        if (!menu) {
            closeAll();
            btn.classList.add('active');
            return;
        }

        const willOpen = !menu.classList.contains('open');

        closeAll();

        root.querySelectorAll('.workspace-menu.open').forEach(m => m.classList.remove('open'));
        root.querySelectorAll('.workspace-name.active').forEach(b => b.classList.remove('active'));

        if (willOpen) {
            menu.classList.add('open');
            btn.classList.add('active');

            const first = menu.querySelector('a.nav-btn[target="mainFrame"]');
            if (first) first.click();
        }
    });

    root.addEventListener('click', (e) => {
        const link = e.target.closest('a.nav-btn');
        if (!link || !root.contains(link)) return;
        root.querySelectorAll('a.nav-btn.active').forEach(a => a.classList.remove('active'));
        link.classList.add('active');
    });
});