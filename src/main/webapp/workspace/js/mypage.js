document.addEventListener('DOMContentLoaded', () => {
    const editBtn = document.getElementById('editBtn');
    const deleteBtn = document.getElementById('deleteBtn');
    const confirmModal = document.getElementById('confirmModal');
    const okConfirm = document.getElementById('okConfirm');
    const cancelConfirm = document.getElementById('cancelConfirm');

    const openModal = (modal) => {
        modal.classList.add('open');
        modal.removeAttribute('aria-hidden');
        document.body.style.overflow = 'hidden';
    };

    const closeModal = (modal) => {
        modal.classList.remove('open');
        modal.setAttribute('aria-hidden', 'true');
        document.body.style.overflow = '';
    };

    // 개인정보 변경 페이지로 이동
    editBtn?.addEventListener('click', () => {
        location.href = '/workspace/personal-info.jsp';
    });

    // 탈퇴 확인 모달 열기
    deleteBtn?.addEventListener('click', () => {
        openModal(confirmModal);
    });

    // 모달 닫기
    cancelConfirm?.addEventListener('click', () => {
        closeModal(confirmModal);
    });

    // 확인 클릭 시 폼 제출 (서버로 POST)
    okConfirm?.addEventListener('click', () => {
        closeModal(confirmModal);

        const form = document.createElement('form');
        form.method = 'POST';
        form.action = '/front';
        form.style.display = 'none';

        const keyInput = document.createElement('input');
        keyInput.type = 'hidden';
        keyInput.name = 'key';
        keyInput.value = 'member';

        const methodInput = document.createElement('input');
        methodInput.type = 'hidden';
        methodInput.name = 'methodName';
        methodInput.value = 'delete';

        form.appendChild(keyInput);
        form.appendChild(methodInput);
        document.body.appendChild(form);

        form.submit();
    });
});
