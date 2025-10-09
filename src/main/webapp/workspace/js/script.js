// 공지 상세 팝업창
document.querySelectorAll('.notice-card').forEach((card) => {
    card.addEventListener('click', () => {
        const modal = document.getElementById('noticeModal');
        const title = card.querySelector('strong').innerText;
        const content = card.querySelector('p').innerText;
        const detail = card.dataset.detail || ''; // 새로 추가된 상세내용

        document.getElementById('modalTitle').innerText = title;
        document.getElementById('modalContent').innerHTML = `
            <p>${content}</p>
            ${detail ? `<hr><p>${detail}</p>` : ''}
        `;
        modal.style.display = 'flex';
        document.body.style.overflow = 'hidden'; // 스크롤 잠금
    });
});

document.querySelector('.modal-close').addEventListener('click', closeModal);
window.addEventListener('click', (e) => {
    if (e.target.id === 'noticeModal') closeModal();
});

function closeModal() {
    const modal = document.getElementById('noticeModal');
    modal.style.display = 'none';
    document.body.style.overflow = ''; // 스크롤 복원
}

//////////////////////////////////////////////////////////////////////////////////////////////////////

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



////////////////////////////////////////////////////////////////////


// 난이도별 색 변경
document.querySelectorAll('.difficulty-level').forEach((el) => {
    const firstChar = el.textContent.trim().charAt(0);
    if (firstChar === 'G') el.classList.add('gold');
    else if (firstChar === 'S') el.classList.add('silver');
    else if (firstChar === 'B') el.classList.add('bronze');
});

////////////////////////////////////////////////////////////////////

// 프로필 박스 클릭 시 마이페이지로 이동
document.addEventListener('DOMContentLoaded', () => {
    const profileBox = document.getElementById('profileBox');
    if (profileBox) {
        profileBox.addEventListener('click', () => {
            // ✅ Ajax 기반 SPA라면 main-content 교체로
            fetch('mypage.jsp')
                .then(res => res.text())
                .then(html => {
                    document.getElementById('mainContent').innerHTML = html;
                })
                .catch(() => {
                    alert('마이페이지를 불러올 수 없습니다.');
                });

        });
    }
});

document.addEventListener('DOMContentLoaded', () => {
    // 워크스페이스 토글
    document.querySelectorAll('.workspace-name').forEach(btn => {
        btn.addEventListener('click', () => {
            const menu = btn.nextElementSibling;
            menu.classList.toggle('open');
            btn.classList.toggle('active');
        });
    });

    // 메뉴 클릭 시 페이지 로드
    document.querySelectorAll('.nav-btn').forEach(btn => {
        btn.addEventListener('click', async () => {
            const page = btn.dataset.page;
            try {
                const res = await fetch(`${page}.jsp`);
                const html = await res.text();
                document.getElementById('mainContent').innerHTML = html;
            } catch (err) {
                alert('페이지를 불러올 수 없습니다.');
            }
        });
    });
});

document.addEventListener('DOMContentLoaded', () => {
    const main = document.getElementById('mainContent');
    const navButtons = document.querySelectorAll('.nav-btn');
    const homeButton = document.querySelector('[data-page="home"]');

    // ✅ 초기 상태: 워크스페이스 홈 활성화
    if (homeButton) homeButton.classList.add('active');

    // ✅ 사이드바 버튼 클릭 시 페이지 전환
    navButtons.forEach(btn => {
        btn.addEventListener('click', async () => {
            const page = btn.dataset.page;
            try {
                const res = await fetch(`${page}.jsp`);
                const html = await res.text();
                main.innerHTML = html;
                if (window.wsSettingsMount) window.wsSettingsMount(main.querySelector('#ws-settings-root'));

                resetJS(); // 새 페이지 JS 재바인딩
            } catch (e) {
                main.innerHTML = `<p style="color:red">❌ 페이지를 불러오지 못했습니다: ${page}.jsp</p>`;
            }

            // 🔸 모든 버튼 비활성화 후 클릭된 버튼만 활성화
            navButtons.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
        });
    });

    // ✅ 프로필 박스 클릭 시 마이페이지로 이동
    const profileBox = document.getElementById('profileBox');
    if (profileBox) {
        profileBox.addEventListener('click', async () => {
            try {
                const res = await fetch('mypage.jsp');
                const html = await res.text();
                main.innerHTML = html;
                resetJS();
            } catch (err) {
                alert('마이페이지를 불러올 수 없습니다.');
            }

            // 🔸 마이페이지 클릭 시 모든 사이드바 버튼 비활성화
            navButtons.forEach(b => b.classList.remove('active'));
        });
    }
});

// 현재 구조 문제로 fetch로 불러오면 js다시 실행시켜야함
function resetJS() {
    // 공지 클릭 -> 모달
    document.querySelectorAll('.notice-card').forEach(card => {
        card.addEventListener('click', () => {
            const modal = document.getElementById('noticeModal');
            const title = card.querySelector('strong').innerText;
            const content = card.querySelector('p').innerText;
            document.getElementById('modalTitle').innerText = title;
            document.getElementById('modalContent').innerHTML = `<p>${content}</p>`;
            modal.style.display = 'flex';
        });
    });

    // 공지 등록 버튼
    document.querySelector('.register-btn')?.addEventListener('click', () => {
        alert('공지 등록 모달이 열릴 예정입니다.');
    });

    // 정렬 버튼
    document.querySelector('.sort-btn')?.addEventListener('click', () => {
        alert('정렬 기능 실행');
    });
}

// 수정 버튼
document.querySelector('.notice-edit-btn')?.addEventListener('click', () => {
    const title = document.getElementById('modalTitle').innerText;
    alert(`🔧 '${title}' 공지를 수정합니다.`);
});

// 삭제 버튼
document.querySelector('.notice-delete-btn')?.addEventListener('click', () => {
    const title = document.getElementById('modalTitle').innerText;
    const confirmDelete = confirm(`❗ '${title}' 공지를 삭제하시겠습니까?`);
    if (confirmDelete) {
        alert('삭제 완료!');
        closeModal();
    }
});