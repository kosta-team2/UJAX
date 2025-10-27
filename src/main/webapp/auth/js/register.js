// register.js — 최종판 (핵심 변경: submit에서 자동 검증 시도)
(function () {
    const $ = (s, r = document) => r.querySelector(s);

    function getCtx() {
        const m = location.pathname.match(/^(.+?)\/auth\/register\.jsp$/);
        return m ? m[1] : '';
    }

    const CTX = getCtx();

    const msgFrom = (j) => (j?.data ?? j?.message ?? j?.msg ?? j?.result ?? j?.ok ?? '');
    const errFrom = (j) => (j?.errorMessage ?? j?.error ?? j?.message ?? '');

    // ---- PW confirm ----
    const pwd = $('#password');
    const pwc = $('#passwordConfirm');
    const icon = $('#pw-check-icon');
    const showCheck = (on) => {
        icon?.classList.toggle('show', !!on);
        pwc?.classList.toggle('has-adornment', !!on);
    };
    const syncPw = () => {
        const p = pwd?.value || '', c = pwc?.value || '';
        showCheck(p && c && p === c);
    };
    pwc?.addEventListener('input', () => {
        pwc.classList.remove('error-ph');
        pwc.placeholder = '';
        syncPw();
    });
    pwd?.addEventListener('input', syncPw);
    pwc?.addEventListener('blur', () => {
        const p = pwd?.value || '', c = pwc?.value || '';
        if (p && c && p !== c) {
            pwc.value = '';
            pwc.placeholder = '비밀번호가 불일치합니다. 다시 입력해주세요';
            pwc.classList.add('error-ph');
            showCheck(false);
        }
    });

    // ---- Email verify flow ----
    const email = $('#email');
    const sendBtn = $('#sendCodeBtn');
    const wrap = $('#emailCodeWrap');
    const codeInp = $('#emailCode');
    const verifyBtn = $('#btnConfirmEmail');
    let isVerified = false;

    const openCode = () => wrap?.classList.add('open');
    const closeCode = () => wrap?.classList.remove('open');

    function hydrateFromHash() {
        if (location.hash === '#email-verified') {
            isVerified = true;
            email?.setAttribute('data-verified', '1');
            closeCode();
        }
    }

    window.addEventListener('hashchange', hydrateFromHash);
    hydrateFromHash();

    sendBtn?.addEventListener('click', async () => {
        const v = (email?.value || '').trim();
        if (!v) {
            email?.focus();
            return;
        }
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/.test(v)) {
            email?.reportValidity?.();
            alert('올바른 이메일 형식이 아닙니다.');
            return;
        }
        openCode();
        codeInp?.focus();
        try {
            const res = await fetch(`${CTX}/ajax?key=auth&methodName=sendSignupCode`, {
                method: 'POST', headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
                body: new URLSearchParams({email: v}).toString()
            });
            const raw = await res.text();
            let json;
            try {
                json = JSON.parse(raw);
            } catch {
            }
            alert(res.ok ? (msgFrom(json) || '인증코드를 전송했습니다. 10분 이내에 입력해 주세요.')
                : (errFrom(json) || raw?.trim() || `요청 실패 (${res.status})`));
        } catch (e) {
            console.error(e);
            alert('네트워크 오류가 발생했습니다.');
        }
    });

    async function verifyOnServer(vEmail, code) {
        const res = await fetch(`${CTX}/ajax?key=auth&methodName=verifySignupCode`, {
            method: 'POST', headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
            body: new URLSearchParams({email: vEmail, code}).toString()
        });
        const raw = await res.text();
        let json;
        try {
            json = JSON.parse(raw);
        } catch {
        }
        if (res.ok) {
            isVerified = true;
            email?.setAttribute('data-verified', '1');
            location.hash = 'email-verified';
            closeCode();
            alert(msgFrom(json) || '이메일 인증이 완료되었습니다.');
            return true;
        } else {
            alert(errFrom(json) || raw?.trim() || `요청 실패 (${res.status})`);
            return false;
        }
    }

    verifyBtn?.addEventListener('click', async () => {
        const vEmail = (email?.value || '').trim();
        const code = (codeInp?.value || '').trim();
        if (!code) {
            codeInp?.focus();
            return;
        }
        try {
            await verifyOnServer(vEmail, code);
        } catch (e) {
            console.error(e);
            alert('네트워크 오류가 발생했습니다.');
        }
    });

    // ---- Submit (자동 검증 시도 추가) ----
    const form = $('#registerForm');
    const nickname = $('#nickname');

    form?.addEventListener('submit', async (e) => {
        e.preventDefault();

        if (!email?.checkValidity?.() || !(email.value || '').trim()) {
            email?.reportValidity?.();
            return;
        }

        let verifiedNow = isVerified || email?.dataset.verified === '1' || location.hash === '#email-verified';
        if (!verifiedNow) {
            const vEmail = (email?.value || '').trim();
            const code = (codeInp?.value || '').trim();
            if (code) {
                try {
                    const ok = await verifyOnServer(vEmail, code);
                    verifiedNow = ok;
                } catch (e) {
                    console.error(e);
                }
            }
        }
        if (!verifiedNow) {
            openCode();
            codeInp?.focus();
            return;
        }

        if (!pwd?.checkValidity?.() || !(pwd.value || '').trim()) {
            pwd?.reportValidity?.();
            return;
        }
        if (!pwc?.value || pwc.value !== pwd.value) {
            pwc?.focus();
            return;
        }
        if (!nickname?.checkValidity?.() || !(nickname.value || '').trim()) {
            nickname?.reportValidity?.();
            return;
        }

        form.action = `${CTX}/front?key=member&methodName=signup`;
        form.method = 'post';
        form.submit();
    });
})();
