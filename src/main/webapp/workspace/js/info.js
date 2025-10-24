// ---------- helpers ----------
const $  = (sel, root = document) => root.querySelector(sel);
const $$ = (sel, root = document) => Array.from(root.querySelectorAll(sel));
const show = (el) => { if (el) el.hidden = false; };
const hide = (el) => { if (el) el.hidden = true; };

// ---------- confirm modal (promise 기반) ----------
function openConfirm(message) {
    const modal   = $('#ws-confirmModal');
    const msgEl   = $('#ws-confirmMessage');
    const yesBtn  = $('#ws-confirmYes');
    const noBtn   = $('#ws-confirmNo');

    // 모달이 없으면 브라우저 confirm fallback
    if (!modal || !msgEl || !yesBtn || !noBtn) {
        return Promise.resolve(window.confirm(message));
    }

    msgEl.textContent = message;
    show(modal);

    return new Promise((resolve) => {
        const onYes = () => cleanup(true);
        const onNo  = () => cleanup(false);
        const onKey = (e) => { if (e.key === 'Escape') cleanup(false); };

        function cleanup(result) {
            hide(modal);
            yesBtn.removeEventListener('click', onYes);
            noBtn.removeEventListener('click', onNo);
            document.removeEventListener('keydown', onKey);
            resolve(result);
        }

        yesBtn.addEventListener('click', onYes);
        noBtn.addEventListener('click', onNo);
        document.addEventListener('keydown', onKey);
    });
}

// ---------- 멤버 초대 모달 ----------
(function initInviteModal() {
    const openBtn   = $('#ws-openInvite');
    const modal     = $('#ws-inviteModal');
    const cancelBtn = $('#ws-inviteCancel');
    const okBtn     = $('#ws-inviteOk');
    const emailInp  = $('#ws-inviteEmail');

    if (!openBtn || !modal) return;

    // 열기
    openBtn.addEventListener('click', () => {
        show(modal);
        if (emailInp) emailInp.focus();
        // 최초엔 OK 버튼 상태를 입력값 기준으로 갱신
        if (okBtn && emailInp) okBtn.disabled = !emailInp.checkValidity();
    });

    // 닫기
    cancelBtn?.addEventListener('click', () => hide(modal));

    // 바깥 클릭 시 닫기 (오버레이 클릭)
    modal.addEventListener('click', (e) => {
        if (e.target === modal) hide(modal);
    });

    // 이메일 유효성에 따라 OK 버튼 enable/disable
    emailInp?.addEventListener('input', () => {
        if (okBtn) okBtn.disabled = !emailInp.checkValidity();
    });

    // OK 클릭 (실제 초대 API/submit은 추후 구현)
    okBtn?.addEventListener('click', () => {
        if (okBtn.disabled) return;
        // TODO: 초대 로직 붙이기 (폼 submit 또는 fetch)
        // 현재는 모달만 닫고 안내
        alert('초대 기능은 준비중입니다.');
        hide(modal);
    });
})();

// ---------- 폼/버튼 confirm 처리 ----------
document.addEventListener('DOMContentLoaded', () => {
    const settingsForm = $('#ws-settingsForm');
    const applyBtn     = $('#ws-applyBtn');

    // 1) 기본 정보 변경
    if (applyBtn && settingsForm) {
        applyBtn.addEventListener('click', async (e) => {
            e.preventDefault();
            const ok = await openConfirm('정말 변경하시겠습니까?');
            if (ok) settingsForm.submit();
        });
    }

    // 2) 리더 위임
    $$('form[action*="methodName=updateRole"]').forEach((f) => {
        f.addEventListener('submit', async (e) => {
            e.preventDefault();
            const ok = await openConfirm('정말 변경하시겠습니까?');
            if (ok) f.submit();
        });
    });

    // 3) 추방
    $$('form[action*="methodName=kickUser"]').forEach((f) => {
        f.addEventListener('submit', async (e) => {
            e.preventDefault();
            const ok = await openConfirm('정말 추방하시겠습니까?');
            if (ok) f.submit();
        });
    });

    // 4) 워크스페이스 나가기
    $$('form[action*="methodName=exit"]').forEach((f) => {
        // 해당 폼에는 #ws-leaveWorkspaceBtn 버튼이 있음
        const btn = $('#ws-leaveWorkspaceBtn', f) || $('button[type="submit"]', f);
        if (!btn) return;
        btn.addEventListener('click', async (e) => {
            e.preventDefault();
            const ok = await openConfirm('정말 해당 워크스페이스에서 탈퇴하시겠습니까?');
            if (ok) f.submit();
        });
    });

    // 5) 워크스페이스 삭제
    $$('form[action*="methodName=delete"]').forEach((f) => {
        const btn = $('#ws-deleteWorkspaceBtn', f) || $('button[type="submit"]', f);
        if (!btn) return;
        btn.addEventListener('click', async (e) => {
            e.preventDefault();
            const ok = await openConfirm('정말 해당 워크스페이스를 삭제하시겠습니까?');
            if (ok) f.submit();
        });
    });
});
