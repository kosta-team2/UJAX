// teamChart.js
window.initTeamChart = function () {
    const monthlyGrass = document.getElementById('monthlyGrass');
    const monthTitle   = document.getElementById('monthTitle');
    const prevBtn      = document.getElementById('prevMonth');
    const nextBtn      = document.getElementById('nextMonth');

    if (!monthlyGrass || !monthTitle || !prevBtn || !nextBtn) return; // 홈이 아니면 스킵

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
        const firstDay = new Date(year, month, 1);
        const lastDay  = new Date(year, month + 1, 0);
        const totalDays = lastDay.getDate();

        const data = generateMockData(totalDays);
        monthTitle.textContent = `${year}년 ${month + 1}월`;

        for (let day = 1; day <= totalDays; day++) {
            const cell = document.createElement('div');
            cell.className = 'grass-cell level-' + (data[day - 1] ?? 0);
            cell.title = `${month + 1}/${day}`;
            monthlyGrass.appendChild(cell);
        }
    }

    // 중복 바인딩 방지: 버튼에 once 옵션
    prevBtn.addEventListener('click', () => {
        currentDate.setMonth(currentDate.getMonth() - 1);
        renderMonth(currentDate);
    }, { once: true });

    nextBtn.addEventListener('click', () => {
        currentDate.setMonth(currentDate.getMonth() + 1);
        renderMonth(currentDate);
    }, { once: true });

    renderMonth(currentDate);
};
