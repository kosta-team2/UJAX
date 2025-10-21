document.addEventListener('DOMContentLoaded', () => {
    const editBtn = document.getElementById('editBtn');
    const deleteBtn = document.getElementById('deleteBtn');
    const confirmModal = document.getElementById('confirmModal');
    const resultModal = document.getElementById('resultModal');
    const okConfirm = document.getElementById('okConfirm');
    const cancelConfirm = document.getElementById('cancelConfirm');
    const closeResult = document.getElementById('closeResult');

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

    editBtn?.addEventListener('click', () => {
        top.location.href = '/workspace/personal-info.jsp';
    });

    deleteBtn?.addEventListener('click', () => {
        openModal(confirmModal);
    });

    cancelConfirm?.addEventListener('click', () => {
        closeModal(confirmModal);
    });

    okConfirm?.addEventListener('click', () => {
        closeModal(confirmModal);

        const form = document.createElement('form');
        form.method = 'POST';
        form.action = '/front';
        form.style.display = 'none';

        const keyInput = document.createElement('input');
        keyInput.type = 'hidden';
        keyInput.name = 'key';
        keyInput.value = 'user';

        const methodInput = document.createElement('input');
        methodInput.type = 'hidden';
        methodInput.name = 'methodName';
        methodInput.value = 'delete';

        form.appendChild(keyInput);
        form.appendChild(methodInput);
        document.body.appendChild(form);

        form.submit();

        // 혹시 redirect 되지 않는 경우를 대비한 보조 처리
        openModal(resultModal);
    });

    // 이후에 iframe 처리할거라 top.location을 로그인 페이지로 설정.
    closeResult?.addEventListener('click', () => {
        closeModal(resultModal);
        top.location.href = '/auth/login.jsp';
    });
});
