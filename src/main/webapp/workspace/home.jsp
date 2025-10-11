<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="css/notice.css">
<link rel="stylesheet" href="css/teamChart.css">
<link rel="stylesheet" href="css/problem.css">
<main class="main-content">
    <section class="section notice-section">
        <h2>📢 팀 공지</h2>
        <div class="notice-grid" id="noticeGrid">

        </div>
    </section>


    <%--todo  팀 차트 수정 예정  --%>
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
    <%--  팀 차트 수정 예정  --%>

    <section class="section">
        <div class="section-header">
            <h3>알고리즘 문제</h3>
        </div>

        <div class="problem-grid" id="homeProblemGrid">

        </div>
    </section>

</main>
