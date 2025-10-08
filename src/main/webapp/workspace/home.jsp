<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="css/notice.css">
<link rel="stylesheet" href="css/teamChart.css">
<link rel="stylesheet" href="css/problem.css">
<main class="main-content">
    <section class="section notice-section">
        <h2>📢 팀 공지</h2>
        <div class="notice-grid">
            <div class="notice-card">
                <strong>스터디 일정 변경</strong>
                <p>이번 주 금요일 20:00 → 토요일 20:00<br>자세한 내용은 슬랙 공지 확인!</p>
            </div>

            <div class="notice-card">
                <strong>주간 회고 업로드</strong>
                <p>일요일 23:00까지 업로드 부탁드립니다.<br>이번 주는 회고 질문이 새로 추가되었습니다.<br>
                    요일 23:00까지 업로드 부탁드립니다.<br>이번 주는 회고 질문이 새로 추가되었습니다.<br>요일 23:00까지 업로드 부탁드립니다.<br>이번 주는 회고 질문이 새로 추가되었습니다.<br>요일 23:00까지 업로드 부탁드립니다.<br>이번 주는 회고 질문이 새로 추가되었습니다.<br>요일 23:00까지 업로드 부탁드립니다.<br>이번 주는 회고 질문이 새로 추가되었습니다.<br>요일 23:00까지 업로드 부탁드립니다.<br>이번 주는 회고 질문이 새로 추가되었습니다.<br>요일 23:00까지 업로드 부탁드립니다.<br>이번 주는 회고 질문이 새로 추가되었습니다.<br>요일 23:00까지 업로드 부탁드립니다.<br>이번 주는 회고 질문이 새로 추가되었습니다.<br>요일 23:00까지 업로드 부탁드립니다.<br>이번 주는 회고 질문이 새로 추가되었습니다.<br>요일 23:00까지 업로드 부탁드립니다.<br>이번 주는 회고 질문이 새로 추가되었습니다.<br>요일 23:00까지 업로드 부탁드립니다.<br>이번 주는 회고 질문이 새로 추가되었습니다.<br>요일 23:00까지 업로드 부탁드립니다.<br>이번 주는 회고 질문이 새로 추가되었습니다.<br>요일 23:00까지 업로드 부탁드립니다.<br>이번 주는 회고 질문이 새로 추가되었습니다.<br>요일 23:00까지 업로드 부탁드립니다.<br>이번 주는 회고 질문이 새로 추가되었습니다.<br>요일 23:00까지 업로드 부탁드립니다.<br>이번 주는 회고 질문이 새로 추가되었습니다.<br>요일 23:00까지 업로드 부탁드립니다.<br>이번 주는 회고 질문이 새로 추가되었습니다.<br>요일 23:00까지 업로드 부탁드립니다.<br>이번 주는 회고 질문이 새로 추가되었습니다.<br></p>
            </div>

            <div class="notice-card">
                <strong>코테 모의전</strong>
                <p>다음 주 수요일 21:00에 진행됩니다.<br>참가자 명단은 노션에서 확인하세요.</p>
            </div>
        </div>
    </section>



    <section class="section">
        <h2>팀 차트</h2>

        <div class="chart-grid chart-grid-3">

            <div class="grass-wrapper">
                <div class="grass-header">
                    <button id="prevMonth" class="month-btn" aria-label="이전 달">◀</button>
                    <span id="monthTitle"></span>
                    <button id="nextMonth" class="month-btn" aria-label="다음 달">▶</button>
                </div>
                <div class="grass-grid" id="monthlyGrass"></div>
            </div>

            <div class="mini-stat">
                <span>이번 주 풀이 수</span><br>
                <strong>87</strong><br>
                <small>+12 vs last week</small>
            </div>

            <div class="mini-stat">
                <span>평균 정답률</span><br>
                <strong>74%</strong><br>
                <small>-3% vs last week</small>
            </div>
        </div>
    </section>

    <section class="section">
        <div class="section-header">
            <h3>알고리즘 문제</h3>
        </div>

        <div class="problem-grid">

            <div class="problem-card">
                <div class="card-top">
                    <strong class="problem-title">괄호의 값</strong>
                    <div class="card-meta">
                        <div class="status-badge not-submitted">미제출</div>
                        <span class="difficulty-level">G5</span>
                    </div>
                </div>
                <div class="tags">
                    <span>스택</span>
                    <span>시뮬</span>
                </div>
                <div class="card-bottom">
                    <div class="bottom-left">
                        <div class="deadline"><i class="fa-regular fa-calendar"></i> 마감: 25-10-06</div>
                        <div class="submit-count"><i class="fa-solid fa-user-group"></i> 제출자 5명</div>
                    </div>
                    <button class="go-btn" onclick="location.href='problem.html?id=1'">이동하기</button>
                </div>
            </div>

            <div class="problem-card">
                <div class="card-top">
                    <strong class="problem-title">이진 트리 순회</strong>
                    <div class="card-meta">
                        <div class="status-badge submitted">제출완료</div>
                        <span class="difficulty-level">S2</span>
                    </div>
                </div>
                <div class="tags">
                    <span>트리</span>
                    <span>DFS</span>
                </div>
                <div class="card-bottom">
                    <div class="bottom-left">
                        <div class="deadline"><i class="fa-regular fa-calendar"></i> 마감: 25-10-07</div>
                        <div class="submit-count"><i class="fa-solid fa-user-group"></i> 제출자 8명</div>
                    </div>
                    <button class="go-btn" onclick="location.href='problem.html?id=2'">이동하기</button>
                </div>
            </div>

            <div class="problem-card">
                <div class="card-top">
                    <strong class="problem-title">괄호 제거</strong>
                    <div class="card-meta">
                        <div class="status-badge not-submitted">미제출</div>
                        <span class="difficulty-level">S5</span>
                    </div>
                </div>
                <div class="tags">
                    <span>문자열</span>
                    <span>스택</span>
                </div>
                <div class="card-bottom">
                    <div class="bottom-left">
                        <div class="deadline"><i class="fa-regular fa-calendar"></i> 마감: 25-10-08</div>
                        <div class="submit-count"><i class="fa-solid fa-user-group"></i> 제출자 3명</div>
                    </div>
                    <button class="go-btn" onclick="location.href='problem.html?id=3'">이동하기</button>
                </div>
            </div>

            <div class="problem-card">
                <div class="card-top">
                    <strong class="problem-title">연속합</strong>
                    <div class="card-meta">
                        <div class="status-badge submitted">제출완료</div>
                        <span class="difficulty-level">G4</span>
                    </div>
                </div>
                <div class="tags">
                    <span>DP</span>
                </div>
                <div class="card-bottom">
                    <div class="bottom-left">
                        <div class="deadline"><i class="fa-regular fa-calendar"></i> 마감: 25-10-09</div>
                        <div class="submit-count"><i class="fa-solid fa-user-group"></i> 제출자 11명</div>
                    </div>
                    <button class="go-btn" onclick="location.href='problem.html?id=4'">이동하기</button>
                </div>
            </div>

        </div>
    </section>

</main>
