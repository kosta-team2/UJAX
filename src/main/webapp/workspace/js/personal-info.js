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
        if (newPw.value.length < 8){
            e.preventDefault();
            alert("비밀번호는 8자리 이상으로만 설정 가능합니다.");
            newPw.focus();
            return;
        }
        // 검증 통과 시 동기 전송
    });

    backBtn.addEventListener('click', () => {
        // history.back();
        location.href = '/front?key=member&methodName=getInfo';
    });
});
