(() => {
    console.info('[problem-register] loaded');

    function syncAlarmUI(form) {
        if (!form) return;
        const options = form.querySelector('#alarmOptions');
        const alarmAt = form.querySelector('#alarmAt');
        const checked = form.querySelector('input[name="alarm"]:checked');
        const isOn = !!checked && checked.value === 'on';

        if (options) {
            options.hidden = !isOn;             // 속성 기반
            options.classList.toggle('show', isOn); // 클래스 기반 (CSS 충돌 대비)
        }
        if (alarmAt) {
            alarmAt.disabled = !isOn;  // off면 전송 안 됨
            alarmAt.required = isOn;
        }
    }

    function bindForm(form) {
        if (!form || form.dataset._alarmBound === '1') return;
        form.dataset._alarmBound = '1';

        syncAlarmUI(form);

        const onAny = () => syncAlarmUI(form);
        form.addEventListener('change', (e) => {
            const t = e.target;
            if (t && t.name === 'alarm') onAny();
        });
        form.addEventListener('input', (e) => {
            const t = e.target;
            if (t && t.name === 'alarm') onAny();
        });
        form.addEventListener('click', (e) => {
            // 라벨 클릭이 들어와도 반응하도록 보강
            const label = e.target.closest?.('.radio-item');
            if (label && label.querySelector('input[name="alarm"]')) {
                // 브라우저 토글 직후에 동기화
                setTimeout(onAny, 0);
            }
        });

        console.info('[problem-register] bound form');
    }

    function bindNow() {
        const forms = document.querySelectorAll('#registerForm');
        forms.forEach(bindForm);
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', bindNow, {once: true});
    } else {
        bindNow();
    }

    const mo = new MutationObserver(() => bindNow());
    mo.observe(document.documentElement, {childList: true, subtree: true});

    let kicks = 0;
    const kick = setInterval(() => {
        bindNow();
        document.querySelectorAll('#registerForm').forEach(syncAlarmUI);
        if (++kicks > 20) clearInterval(kick); // 2초 정도만 보강
    }, 100);

    window.__problemRegisterSync = bindNow;
})();