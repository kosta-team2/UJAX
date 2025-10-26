// ---------- helpers ----------
const $  = (sel, root = document) => root.querySelector(sel);
const $$ = (sel, root = document) => Array.from(root.querySelectorAll(sel));
const show = (el) => { if (el) el.hidden = false; };
const hide = (el) => { if (el) el.hidden = true; };

function getContextPath() {
    const f = $('#ws-settingsForm');
    if (!f) return '';
    try {
        const u = new URL(f.action, window.location.href);
        return u.pathname.replace(/\/front.*$/, '');
    } catch {
        return '';
    }
}

// ---------- confirm modal (promise 기반) ----------
function openConfirm(message) {
    const modal   = $('#ws-confirmModal');
    const msgEl   = $('#ws-confirmMessage');
    const yesBtn  = $('#ws-confirmYes');
    const noBtn   = $('#ws-confirmNo');

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
    const rootEl    = $('#ws-settings-root');
    const wsId      = rootEl?.dataset.wsId;
    const ctx       = getContextPath(); // ex) "" 또는 "/server-1.0-SNAPSHOT" 같은 값

    if (!openBtn || !modal) return;

    // 열기
    openBtn.addEventListener('click', () => {
        show(modal);
        if (emailInp) emailInp.focus();
        if (okBtn && emailInp) okBtn.disabled = !emailInp.checkValidity();
    });

    // 닫기
    cancelBtn?.addEventListener('click', () => hide(modal));

    // 바깥 클릭 시 닫기
    modal.addEventListener('click', (e) => {
        if (e.target === modal) hide(modal);
    });

    // 이메일 유효성 따라 OK 상태
    emailInp?.addEventListener('input', () => {
        if (okBtn) okBtn.disabled = !emailInp.checkValidity();
    });

    // OK → 초대 API 호출
    okBtn?.addEventListener('click', async () => {
        if (okBtn.disabled) return;
        if (!wsId) {
            alert('워크스페이스 ID를 찾을 수 없어요.');
            return;
        }
        const email = emailInp.value.trim();
        const params = new URLSearchParams();
        params.set('workspaceId', wsId);
        params.set('email', email);

        try {
            const url = `${ctx}/ajax?key=workspace&methodName=invite`;
            const res = await fetch(url, {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' },
                body: params.toString()
            });

            const text = await res.text();
            let data;
            try {
                data = JSON.parse(text);
            } catch (e) {
                console.error('Non-JSON response', res.status, text);
                alert('서버 응답이 올바르지 않습니다.');
                return;
            }

            if (data.errorMessage) {
                alert(data.errorMessage);
            } else {
                alert(data.data || '초대 메일을 보냈습니다.');
                hide(modal);
                if (emailInp) emailInp.value = '';
            }
        } catch (err) {
            console.error(err);
            alert('네트워크 오류가 발생했습니다.');
        }
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
