// todo 전체 갈아 엎을 예정
// ====== 월간 잔디 ======
const monthlyGrass = document.getElementById('monthlyGrass');
const monthTitle = document.getElementById('monthTitle');
let currentDate = new Date();

// mock 데이터 생성 함수
function generateMockData(days) {
    return Array.from({length: days}, () =>
        Math.random() < 0.6 ? 0 : Math.ceil(Math.random() * 4)
    );
}

// 월 렌더링
function renderMonth(date) {
    monthlyGrass.innerHTML = '';

    const year = date.getFullYear();
    const month = date.getMonth();
    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const totalDays = lastDay.getDate();
    const startWeekday = firstDay.getDay(); // 일요일 0 ~ 토요일 6

    const mockData = generateMockData(totalDays);

    // 제목 표시
    monthTitle.textContent = `${year}년 ${month + 1}월`;

    // 공백 채우기 (시작 요일 전)
    for (let i = 0; i < startWeekday; i++) {
        const blank = document.createElement('div');
        monthlyGrass.appendChild(blank);
    }

    // 날짜별 잔디칸
    for (let d = 1; d <= totalDays; d++) {
        const div = document.createElement('div');
        div.classList.add('grass-day');
        const level = mockData[d - 1];
        if (level > 0) div.dataset.level = level;
        div.dataset.date = `${month + 1}월 ${d}일`;
        monthlyGrass.appendChild(div);
    }
}

// 잔디 버튼 이벤트
document.getElementById('prevMonth').addEventListener('click', () => {
    currentDate.setMonth(currentDate.getMonth() - 1);
    renderMonth(currentDate);
});
document.getElementById('nextMonth').addEventListener('click', () => {
    currentDate.setMonth(currentDate.getMonth() + 1);
    renderMonth(currentDate);
});

// 초기 렌더
renderMonth(currentDate);
