// todo 전체 갈아 엎을 예정
window.initTeamChart = function () {
    function tryInit() {
        const monthlyGrass = document.getElementById('monthlyGrass');
        const monthTitle = document.getElementById('monthTitle');
        const prevBtn = document.getElementById('prevMonth');
        const nextBtn = document.getElementById('nextMonth');

        // DOM이 아직 안 생겼으면 다음 프레임에서 재시도
        if (!monthlyGrass || !monthTitle || !prevBtn || !nextBtn) {
            requestAnimationFrame(tryInit);
            return;
        }

        let currentDate = new Date();

        function generateMockData(days) {
            return Array.from({ length: days }, () =>
                Math.random() < 0.6 ? 0 : Math.ceil(Math.random() * 4)
            );
        }

        function renderMonth(date) {
            monthlyGrass.innerHTML = '';
            const year = date.getFullYear();
            const month = date.getMonth();
            const totalDays = new Date(year, month + 1, 0).getDate();
            const data = generateMockData(totalDays);

            monthTitle.textContent = `${year}년 ${month + 1}월`;

            for (let i = 1; i <= totalDays; i++) {
                const cell = document.createElement('div');
                cell.className = 'grass-day';                // ✅ 변경
                cell.dataset.level = data[i - 1] ?? 0;       // ✅ level값은 data-level 속성으로
                cell.dataset.date = `${month + 1}/${i}`;     // ✅ 툴팁용 날짜 표시
                monthlyGrass.appendChild(cell);
            }
        }

        prevBtn.onclick = () => {
            currentDate.setMonth(currentDate.getMonth() - 1);
            renderMonth(currentDate);
        };

        nextBtn.onclick = () => {
            currentDate.setMonth(currentDate.getMonth() + 1);
            renderMonth(currentDate);
        };

        renderMonth(currentDate);
    }

    tryInit();
};
