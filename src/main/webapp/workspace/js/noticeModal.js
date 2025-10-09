document.addEventListener('click', (e) => {
    const card = e.target.closest('.notice-card');
    if (!card) return;

    const modal = document.getElementById('noticeModal');
    const title = card.querySelector('strong')?.innerText || '';
    const content = card.querySelector('p')?.innerText || '';
    const detail = card.dataset.detail || '';

    document.getElementById('modalTitle').innerText = title;
    document.getElementById('modalContent').innerHTML = `
    <p>${content}</p>
    ${detail ? `<hr><p>${detail}</p>` : ''}
  `;
    modal.style.display = 'flex';
    document.body.style.overflow = 'hidden';
});

// 모달 닫기
document.addEventListener('click', (e) => {
    if (e.target.matches('.modal-close') || e.target.id === 'noticeModal') {
        const modal = document.getElementById('noticeModal');
        modal.style.display = 'none';
        document.body.style.overflow = '';
    }
});

// 삭제 버튼
document.addEventListener('click', (e) => {
    if (e.target.matches('.notice-delete-btn')) {
        // todo 삭제처리 삭제 버튼 리더에게만 보이게
    }
});
