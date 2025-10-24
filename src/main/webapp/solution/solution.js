document.addEventListener('DOMContentLoaded', () => {
    // 리더 메뉴 토글
    const leaderBtn = document.getElementById('leaderMenuBtn');
    const leaderMenu = document.getElementById('leaderMenu');
    if (leaderBtn && leaderMenu) {
        leaderBtn.type = 'button';
        leaderBtn.addEventListener('click', (e) => {
            e.preventDefault();
            leaderMenu.classList.toggle('open');
        });
        document.addEventListener('click', (e) => {
            if (!leaderMenu.contains(e.target) && e.target !== leaderBtn) leaderMenu.classList.remove('open');
        });
    }

    // (옵션) 코드/댓글 모달 등 필요한 최소 로직만 여기에 추가

    // (옵션) 스플리터 드래그
    const container = document.getElementById('split');
    const bar = document.getElementById('splitter');
    if (container && bar) {
        const clamp = p => Math.max(20, Math.min(80, p));
        bar.addEventListener('pointerdown', (e) => {
            e.preventDefault();
            const move = (x) => {
                const r = container.getBoundingClientRect();
                const pct = clamp(((x - r.left) / r.width) * 100);
                container.style.setProperty('--split-left', pct + '%');
            };
            const onMove = ev => move(ev.clientX);
            const onUp = () => {
                bar.removeEventListener('pointermove', onMove);
                bar.removeEventListener('pointerup', onUp);
            };
            bar.addEventListener('pointermove', onMove);
            bar.addEventListener('pointerup', onUp, {once: true});
            move(e.clientX);
        });
    }
});