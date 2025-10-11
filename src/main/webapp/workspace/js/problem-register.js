// ❗ 즉시 실행이 아닌, 마운트 함수 형태로 정의
window.registerProblemMount = function (root) {
    if (!root) return;

    const q = (s) => root.querySelector(s);
    const qa = (s) => root.querySelectorAll(s);

    // ==========================
    // 1. 추천 클릭 → 문제번호 자동 입력
    // ==========================
    q(".problem-link")?.addEventListener("click", () => {
        const input = q("#problemNumber");
        if (!input) return;
        input.value = "11724";
        input.focus();
    });

    // ==========================
    // 2. problem.jsp 로드 함수 (뒤로가기 / 등록 완료 시 사용)
    // ==========================
    async function loadProblemPage() {
        const main = document.getElementById('mainContent');
        try {
            const res = await fetch('problem.jsp');
            if (!res.ok) throw new Error(`HTTP ${res.status}`);
            const html = await res.text();
            main.innerHTML = html;

            // ✅ SPA 초기화 호출 (문자열로 page 지정)
            if (typeof reload === 'function') {
                reload('problem');
            } else {
                console.warn('⚠️ reload() 함수가 정의되어 있지 않아 initProblem을 직접 호출합니다.');
                if (typeof window.initProblem === 'function') window.initProblem();
            }

        } catch (e) {
            console.error('❌ 문제 페이지 로드 실패:', e);
            main.innerHTML = '<p style="color:red;">문제 페이지를 불러올 수 없습니다.</p>';
        }
    }

    // ==========================
    // 3. 뒤로가기 버튼 → problem.jsp 로드
    // ==========================
    q("#backBtn")?.addEventListener("click", loadProblemPage);

    // ==========================
    // 4. deadline 최소값 설정
    // ==========================
    const deadline = q("#deadline");
    if (deadline) {
        const pad = (n) => String(n).padStart(2, "0");
        const toLocalDatetimeValue = (d) =>
            `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(
                d.getDate()
            )}T${pad(d.getHours())}:${pad(d.getMinutes())}`;
        const ceilToStep = (date, stepMinutes) => {
            const ms = stepMinutes * 60 * 1000;
            const ceil = Math.ceil(date.getTime() / ms) * ms;
            return new Date(ceil);
        };
        const now = new Date();
        now.setMinutes(now.getMinutes() + 120);
        const minDate = ceilToStep(now, 30);
        const minVal = toLocalDatetimeValue(minDate);
        deadline.min = minVal;
        if (!deadline.value || deadline.value < minVal) deadline.value = minVal;
    }

    // ==========================
    // 5. 알람 옵션 표시 토글
    // ==========================
    const alarmRadios = qa('input[name="alarm"]');
    const alarmOptions = q("#alarmOptions");
    const startHours = q("#startHours");

    function syncAlarmOptions() {
        const val = root.querySelector('input[name="alarm"]:checked')?.value;
        const on = val === "on";
        alarmOptions.classList.toggle("show", on);
        startHours.required = on;
    }
    alarmRadios.forEach((r) => r.addEventListener("change", syncAlarmOptions));
    syncAlarmOptions();

    // 숫자 입력 제약
    startHours?.addEventListener("input", () => {
        const min = Number(startHours.min) || 1;
        const max = Number(startHours.max) || 24;
        let v = parseInt(startHours.value || "1", 10);
        if (isNaN(v) || v < min) v = min;
        if (v > max) v = max;
        startHours.value = String(v);
    });

    // ==========================
    // 6. 폼 제출
    // ==========================
    q("#registerForm")?.addEventListener("submit", async (e) => {
        e.preventDefault();

        const payload = {
            problemNumber: q("#problemNumber")?.value.trim(),
            deadline: deadline?.value,
            alarm:
                root.querySelector('input[name="alarm"]:checked')?.value || "off",
            startHours: startHours?.required ? Number(startHours.value) : null,
        };

        // 입력 검증
        if (!payload.problemNumber) {
            alert("문제 번호를 입력하세요.");
            q("#problemNumber")?.focus();
            return;
        }
        if (!payload.deadline || (deadline && payload.deadline < deadline.min)) {
            alert("제출 기한은 지금으로부터 2시간 이후(30분 단위)만 설정할 수 있습니다.");
            deadline?.focus();
            return;
        }
        if (payload.alarm === "on" && (!payload.startHours || payload.startHours < 1)) {
            alert("알람 시작 시점을 1시간 단위로 입력하세요.");
            startHours?.focus();
            return;
        }

        // 서버 전송 대신 콘솔 확인 (테스트용)
        console.log("submit payload:", payload);

        alert(`등록되었습니다!
- 문제 번호: ${payload.problemNumber}
- 마감: ${payload.deadline}
- 알림: ${payload.alarm.toUpperCase()}${payload.alarm === "on" ? ` (마감 ${payload.startHours}시간 전)` : ""}`);

        // 등록 후 problem.jsp 복귀
        await loadProblemPage();
    });
};
