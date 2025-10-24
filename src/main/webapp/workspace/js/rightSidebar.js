/*
document.addEventListener('DOMContentLoaded', () => {
    const profileBox = document.getElementById('profileBox');
    const main = document.getElementById('mainContent');
    if (!profileBox) return;

    profileBox.addEventListener('click', async (e) => {
        if (!main) return;
        e.preventDefault();

        const base = profileBox.getAttribute('href');
        const url = base + (base.includes('?') ? '&' : '?') + 'fragment=1';

        try {
            const res = await fetch(url);
            const html = await res.text();
            main.innerHTML = html;

            if (typeof reload === 'function') reload('mypage');

            // 사이드바 active 표시
            document.querySelectorAll('.nav-btn').forEach(b => b.classList.remove('active'));
            profileBox.classList.add('active');
            window.scrollTo({top: 0, behavior: 'smooth'});
        } catch (err) {
            console.error(err);
            main.innerHTML = '<p style="color:red;">❌ 마이페이지를 불러올 수 없습니다.</p>';
        }
    });
});*/

// 필요없어져서 주석처리함