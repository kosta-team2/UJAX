document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('pi-form');
    const newPw = document.getElementById('newPassword');
    const confirmPw = document.getElementById('confirmPw');
    const backBtn = document.getElementById('pi-backBtn');

    // 비밀번호 확인 검증
    form.addEventListener('submit', (e) => {
        if (newPw.value !== confirmPw.value) {
            e.preventDefault();
            alert('새 비밀번호가 일치하지 않습니다.');
            confirmPw.focus();
            return;
        }
        // 검증 통과 시 동기 전송
        // TODO : 백에서 비밀번호 검증 로직 만들고 연결해야함.
    });

    backBtn.addEventListener('click', () => {
        location.href = '/workspace/mypage.jsp';
    });
});
