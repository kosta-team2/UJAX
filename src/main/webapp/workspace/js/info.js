// ---------- helpers ----------
const $ = (sel, root = document) => root.querySelector(sel);
const $$ = (sel, root = document) => Array.from(root.querySelectorAll(sel));
const show = (el) => {
    if (el) el.hidden = false;
};
const hide = (el) => {
    if (el) el.hidden = true;
};

function getContextPath() {
    const f = $('#ws-settingsForm');
    if (!f) return '';
    try {
        const u = new URL(f.action, window.location.href);
        return u.pathname.replace(/\/front.*$/, ''); // "/ctx" or ""
    } catch {
        return '';
    }
}

// ---------- confirm modal ----------
function openConfirm(message) {
    const modal = $('#ws-confirmModal');
    const msgEl = $('#ws-confirmMessage');
    const yesBtn = $('#ws-confirmYes');
    const noBtn = $('#ws-confirmNo');

    if (!modal || !msgEl || !yesBtn || !noBtn) {
        return Promise.resolve(window.confirm(message));
    }

    msgEl.textContent = message;
    show(modal);

    return new Promise((resolve) => {
        const onYes = () => cleanup(true);
        const onNo = () => cleanup(false);
        const onKey = (e) => {
            if (e.key === 'Escape') cleanup(false);
        };

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

(function initInviteModal() {
    const openBtn   = document.querySelector('#ws-openInvite');
    const modal     = document.querySelector('#ws-inviteModal');
    const form      = document.querySelector('#ws-inviteForm');
    const cancelBtn = document.querySelector('#ws-inviteCancel');
    const emailInp  = document.querySelector('#ws-inviteEmail');

    const rootEl = document.querySelector('#ws-settings-root');
    const wsId   = rootEl?.dataset.wsId || '';
    const ctx    = getContextPath();

    if (!openBtn || !modal || !form || !emailInp) return;

    openBtn.addEventListener('click', () => {
        form.reset();
        show(modal);
        emailInp.focus();
    });

    cancelBtn.addEventListener('click', () => hide(modal));
    modal.addEventListener('click', (e) => { if (e.target === modal) hide(modal); });

    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        if (!wsId) { alert('워크스페이스 ID를 찾을 수 없어요.'); return; }

        const params = new URLSearchParams({ workspaceId: wsId, email: emailInp.value.trim() });

        try {
            const res = await fetch(`${ctx}/ajax?key=workspace&methodName=invite`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' },
                body: params.toString()
            });

            // 본문은 한 번만 읽는다
            const raw = await res.text();
            let obj; try { obj = JSON.parse(raw); } catch {}

            if (!res.ok) {
                const msg =
                    (obj && (obj.errorMessage || obj.error || obj.message || obj.data)) ||
                    raw.trim() ||
                    `요청 실패 (${res.status})`;
                alert(msg);
                return;
            }

            const okMsg = (obj && (obj.data || obj.message)) || '초대 메일을 보냈습니다.';
            alert(okMsg);
            form.reset();
            hide(modal);

        } catch (err) {
            console.error(err);
            alert('네트워크 오류가 발생했습니다.');
        }
    });
})();

// ---------- 폼/버튼 confirm 처리 ----------
document.addEventListener('DOMContentLoaded', () => {
    const settingsForm = $('#ws-settingsForm');
    const applyBtn = $('#ws-applyBtn');

    if (applyBtn && settingsForm) {
        applyBtn.addEventListener('click', async (e) => {
            e.preventDefault();
            const ok = await openConfirm('정말 변경하시겠습니까?');
            if (ok) settingsForm.submit();
        });
    }

    $$('form[action*="methodName=updateRole"]').forEach((f) => {
        f.addEventListener('submit', async (e) => {
            e.preventDefault();
            const ok = await openConfirm('정말 변경하시겠습니까?');
            if (ok) f.submit();
        });
    });

    $$('form[action*="methodName=kickUser"]').forEach((f) => {
        f.addEventListener('submit', async (e) => {
            e.preventDefault();
            const ok = await openConfirm('정말 추방하시겠습니까?');
            if (ok) f.submit();
        });
    });

    $$('form[action*="methodName=exit"]').forEach((f) => {
        const btn = $('#ws-leaveWorkspaceBtn', f) || $('button[type="submit"]', f);
        if (!btn) return;
        btn.addEventListener('click', async (e) => {
            e.preventDefault();
            const ok = await openConfirm('정말 해당 워크스페이스에서 탈퇴하시겠습니까?');
            if (ok) f.submit();
        });
    });

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
