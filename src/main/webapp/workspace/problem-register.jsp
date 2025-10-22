<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<link rel="stylesheet" id="theme-style" href="../common/css/darkmode.css">
<link rel="stylesheet" href="css/problem-register.css">

<section class="register-problem-section" id="register-problem-fragment">
    <div class="card">
        <!-- 헤더 -->
        <div class="card-header">
            <button class="back-link" id="backBtn">← 돌아가기</button>
            <h1>문제 등록</h1>
        </div>

        <!-- 추천 영역 -->
        <div class="promo" id="recommendBox">
      <span id="recommendText">
        추천: <b class="problem-link">백준 11724</b> — 연결 요소의 개수 (유형: graph)
      </span>
            <button id="refreshRecommend" class="refresh" aria-label="추천 새로고침">↻</button>
        </div>

        <!-- 폼 -->
        <form id="registerForm" class="grid-form" novalidate>
            <!-- 좌측 -->
            <div class="form-col">
                <div class="field">
                    <label for="problemNumber">문제 번호</label>
                    <input
                            id="problemNumber"
                            name="problemNumber"
                            type="text"
                            placeholder="문제 번호를 입력하세요"
                            required
                    />
                </div>

                <div class="field">
                    <label for="deadline">제출 기한</label>
                    <input
                            id="deadline"
                            name="deadline"
                            type="datetime-local"
                            step="1800"
                            required
                    />
                    <p class="hint">현재 시각 기준 <b>2시간 이후</b>만 설정할 수 있습니다.</p>
                </div>
            </div>

            <!-- 우측 -->
            <div class="form-col">
                <div class="field">
                    <label>알람 설정</label>
                    <div class="alarm-pill" role="radiogroup" aria-label="알림 설정">
                        <label class="radio-item">
                            <input type="radio" name="alarm" value="on" />
                            <span class="dot" aria-hidden="true"></span>
                            <span class="txt">On</span>
                        </label>

                        <label class="radio-item">
                            <input type="radio" name="alarm" value="off" checked />
                            <span class="dot" aria-hidden="true"></span>
                            <span class="txt">Off</span>
                        </label>
                    </div>
                </div>

                <!-- ▽ 알람 옵션(ON일 때만 표시) -->
                <div id="alarmOptions" class="alarm-options">
                    <div class="opt">
                        <label for="startHours">마감 몇 시간 전부터</label>
                        <div class="opt-row">
                            <input
                                    id="startHours"
                                    type="number"
                                    min="1"
                                    max="24"
                                    step="1"
                                    value="1"
                                    inputmode="numeric"
                            />
                            <span class="unit">시간 전</span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 제출 버튼 -->
            <div class="actions">
                <button type="submit" class="btn primary">등록</button>
            </div>
        </form>
    </div>
</section>
