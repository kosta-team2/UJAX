// 프로필 박스 클릭 시 마이페이지로 이동
document.addEventListener('DOMContentLoaded', () => {
    const profileBox = document.getElementById('profileBox');
    const main = document.getElementById('mainContent');
    const page = profileBox?.getAttribute('page') || 'mypage';

    if (profileBox) {
        profileBox.addEventListener('click', async () => {
            try {
                // todo mypage 불러오기
				await window.mountMypage?.(main); // JSP 주입 (mypage.js 제공)

                // js 초기화
                if (typeof reload === 'function') reload(page);

                // 사이드바 버튼 활성화 해제
                document.querySelectorAll('.nav-btn').forEach(b => b.classList.remove('active'));
				profileBox.classList.add('active');

                console.log('✅ 마이페이지 로드 완료');
            } catch (err) {
                main.innerHTML = '<p style="color:red;">❌ 마이페이지를 불러올 수 없습니다.</p>';
                console.error(err);
            }
        });
    }
});
