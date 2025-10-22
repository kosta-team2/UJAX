<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String _id = request.getParameter("id");
    if (_id == null || _id.isBlank())
        _id = request.getParameter("productId");
    String id = (_id != null) ? _id : "";
    boolean fragment = "1".equals(request.getParameter("fragment"));

    String ctx = request.getContextPath();
    String pageParam = request.getParameter("page") != null ? request.getParameter("page") : "1";
    String sizeParam = request.getParameter("size") != null ? request.getParameter("size") : "6";
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8"/>
    <title>기프티콘 상세</title>
    <!-- 테마 + 상세 CSS -->
    <link rel="stylesheet" href="<%=ctx%>/common/css/darkmode.css"/>
    <link rel="stylesheet" href="<%=ctx%>/workspace/css/detail.css"/>
</head>
<body>
<% if (!fragment) { %>
<div id="mainContent"><% } %>

    <section class="order-detail" data-product-id="<%= id %>">
        <header class="topbar">
            <h1 class="title">상세 주문 페이지</h1>
            <!-- 목록으로 (동기 이동 기준) -->
            <a class="btn-list" id="backToList"
               href="<%=ctx%>/workspace/giftshop.jsp?page=<%=pageParam%>&size=<%=sizeParam%>">목록으로</a>
        </header>

        <article class="card" role="article" aria-label="상품 상세">
            <div class="thumb">
                <span class="ph">이미지 1:1 영역</span>
                <img src="" alt=""/>
            </div>
            <div class="info">
                <div class="brand" data-field="brand">–</div>
                <h2 class="name" data-field="name">–</h2>
                <div class="price-row">
                    <div class="price" data-field="price">–</div>
                </div>
                <p class="notes">본 이미지는 예시이며, 실제 쿠폰과 다를 수 있습니다.</p>
                <button class="btn-pay" id="payBtn" aria-haspopup="dialog" aria-controls="confirmModal">결제하기</button>
                <div class="chip" data-field="chip" hidden>인기 상품</div>
            </div>
        </article>

        <!-- Confirm -->
        <div class="modal" id="confirmModal" role="dialog" aria-modal="true" aria-hidden="true"
             aria-labelledby="confirmTitle">
            <div class="dialog" role="document">
                <div class="dialog-head">
                    <svg class="icon" viewBox="0 0 24 24" aria-hidden="true">
                        <rect x="2" y="6" width="20" height="12" rx="2"/>
                        <path d="M2 10h20"/>
                    </svg>
                    <h2 class="dialog-title" id="confirmTitle">리워드로 결제</h2>
                </div>
                <div class="dialog-body" id="confirmMsg"></div>
                <div class="dialog-actions">
                    <button class="btn btn-ghost" data-close>취소</button>
                    <button class="btn btn-primary" id="confirmPayBtn">결제하기</button>
                </div>
            </div>
        </div>

        <!-- Insufficient -->
        <div class="modal" id="insufModal" role="dialog" aria-modal="true" aria-hidden="true"
             aria-labelledby="insufTitle">
            <div class="dialog" role="document">
                <div class="dialog-head">
                    <svg class="icon warn" viewBox="0 0 24 24" aria-hidden="true">
                        <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"/>
                        <line x1="12" y1="9" x2="12" y2="13"/>
                        <line x1="12" y1="17" x2="12.01" y2="17"/>
                    </svg>
                    <h2 class="dialog-title" id="insufTitle">리워드가 부족합니다</h2>
                </div>
                <div class="dialog-body" id="insufMsg"></div>
                <div class="dialog-actions">
                    <button class="btn btn-primary" data-close>확인</button>
                </div>
            </div>
        </div>

        <!-- Success -->
        <div class="modal" id="successModal" role="dialog" aria-modal="true" aria-hidden="true"
             aria-labelledby="payTitle">
            <div class="dialog" role="document">
                <div class="dialog-head">
                    <svg class="icon" viewBox="0 0 24 24" aria-hidden="true">
                        <path d="M20 6L9 17l-5-5"/>
                    </svg>
                    <h2 class="dialog-title" id="payTitle">결제가 완료되었습니다</h2>
                </div>
                <div class="dialog-body" id="successMsg"></div>
                <div class="dialog-actions">
                    <button class="btn btn-primary" data-close>확인</button>
                </div>
            </div>
        </div>
    </section>

    <% if (!fragment) { %></div>
<% } %>

<script defer src="js/giftshop-detail.js"></script>

</body>
</html>