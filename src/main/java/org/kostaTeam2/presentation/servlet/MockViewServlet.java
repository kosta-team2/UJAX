package org.kostaTeam2.presentation.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/mock/*")
public class MockViewServlet extends HttpServlet {
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String key = req.getParameter("key");
		String method = req.getParameter("methodName");

		if ("workspace".equals(key) && "getList".equals(method)) {
			List<WorkspaceView> list = List.of(
				new WorkspaceView(1L, "힘들다 쉬게 해주라"),
				new WorkspaceView(2L, "제발 쫌!!!!!")
			);
			req.setAttribute("workspaces", list);
		}

		if ("workspaceHome".equals(key) && "view".equals(method)) {
			long wsId = parseLong(req.getParameter("wsId"), 1L);

			List<NoticeView> notices = List.of(
				new NoticeView(10_000 + wsId, "팀 규칙 업데이트", "코드리뷰 룰이 일부 변경되었습니다."),
				new NoticeView(10_001 + wsId, "이번 주 일정", "수요일 20시에 스터디 진행합니다."),
				new NoticeView(10_002 + wsId, "리팩토링 가이드", "서비스/DAO 레이어 분리 권장안 공유")
			);

			// 통계 (예시)
			int weeklySolved = 87;
			int avgAccuracy = 74;

			// 문제 6개
			List<ProblemCard> problems = new ArrayList<>();
			for (int i = 0; i < 6; i++) {
				boolean submitted = (i % 2 == 0);
				String diff = (i < 2 ? "Gold 5" : (i < 4 ? "Silver 2" : "Bronze 1"));
				String diffClass = (i < 2 ? "gold" : (i < 4 ? "silver" : "")); // bronze는 기본 스타일
				problems.add(new ProblemCard(
					2000 + i,
					"백준 " + (11400 + i),
					diff,
					diffClass,
					submitted,
					LocalDate.now().plusDays(i + 1).toString(),
					12 + i,
					List.of("문자열", "자료구조")
				));
			}

			req.setAttribute("notices", notices);
			req.setAttribute("weeklySolved", weeklySolved);
			req.setAttribute("avgAccuracy", avgAccuracy);
			req.setAttribute("problems", problems);
		}

		if ("problem".equals(key) && "getProblems".equals(method)) {
			long wsId = parseLong(req.getParameter("wsId"), 1L);
			int page = parseInt(req.getParameter("page"), 1);
			int size = parseInt(req.getParameter("size"), 6);

			int total = 73; // 목업 총 건수
			List<ProblemCard> all = new ArrayList<>();
			for (int i = 0; i < total; i++) {
				boolean submitted = (i % 3 == 0);
				String diff = (i % 6 < 2) ? "Gold 5" : (i % 6 < 4 ? "Silver 2" : "Bronze 1");
				String diffClass = (i % 6 < 2) ? "gold" : (i % 6 < 4 ? "silver" : "");
				all.add(new ProblemCard(
					11000 + i,
					"백준 " + (11000 + i),
					diff,
					diffClass,
					submitted,
					LocalDate.now().plusDays((i % 12) + 1).toString(),
					7 + (i % 20),
					List.of("문자열", "자료구조")
				));
			}

			int totalPages = (int)Math.ceil((double)total / size);
			page = Math.max(1, Math.min(page, totalPages));
			int from = (page - 1) * size;
			int to = Math.min(from + size, total);
			List<ProblemCard> pageList = all.subList(from, to);

			// 페이저 윈도우 (5개)
			int window = 5;
			int startPage = Math.max(1, page - window / 2);
			int endPage = Math.min(totalPages, startPage + window - 1);
			startPage = Math.max(1, endPage - window + 1);

			req.setAttribute("problems", pageList);
			req.setAttribute("page", page);
			req.setAttribute("size", size);
			req.setAttribute("totalPages", totalPages);
			req.setAttribute("hasPrev", page > 1);
			req.setAttribute("hasNext", page < totalPages);
			req.setAttribute("prevPage", page - 1);
			req.setAttribute("nextPage", page + 1);
			req.setAttribute("startPage", startPage);
			req.setAttribute("endPage", endPage);
		}

		if ("notice".equals(key) && "getNotices".equals(method)) {
			long wsId = parseLong(req.getParameter("wsId"), 1L);
			int page = parseInt(req.getParameter("page"), 1);
			int size = parseInt(req.getParameter("size"), 6);

			// 총 건수(목업)
			int total = 48;

			// 전체 리스트 생성(목업)
			List<NoticeView> all = new ArrayList<>();
			for (int i = 0; i < total; i++) {
				int no = i + 1;
				String title = "팀 공지 #" + no;
				String content =
					"워크스페이스(" + wsId + ") 공지 내용 " + no + "번입니다.\n" +
						"일정/가이드/업데이트 등 안내드립니다. (" + LocalDate.now().minusDays(i % 7) + ")";
				all.add(new NoticeView(5000 + i, title, content));
			}

			// 페이징
			int totalPages = (int)Math.ceil((double)total / size);
			page = Math.max(1, Math.min(page, totalPages));
			int from = (page - 1) * size;
			int to = Math.min(from + size, total);
			List<NoticeView> pageList = all.subList(from, to);

			// 페이지 윈도우(5개)
			int window = 5;
			int startPage = Math.max(1, page - window / 2);
			int endPage = Math.min(totalPages, startPage + window - 1);
			startPage = Math.max(1, endPage - window + 1);

			// JSP에서 쓰는 속성 세팅 (문제와 동일 키)
			req.setAttribute("notices", pageList);
			req.setAttribute("page", page);
			req.setAttribute("size", size);
			req.setAttribute("totalPages", totalPages);
			req.setAttribute("hasPrev", page > 1);
			req.setAttribute("hasNext", page < totalPages);
			req.setAttribute("prevPage", Math.max(1, page - 1));
			req.setAttribute("nextPage", Math.min(totalPages, page + 1));
			req.setAttribute("startPage", startPage);
			req.setAttribute("endPage", endPage);
		}

		if ("giftshop".equals(key) && "list".equals(method)) {
			int page = parseInt(req.getParameter("page"), 1);
			int size = parseInt(req.getParameter("size"), 8);

			List<ProductView> all = buildProducts();   // 전체 상품 목업
			int total = all.size();

			int totalPages = (int)Math.ceil((double)total / size);
			page = Math.max(1, Math.min(page, Math.max(totalPages, 1)));
			int from = Math.max(0, (page - 1) * size);
			int to = Math.min(from + size, total);
			List<ProductView> pageList = all.subList(from, to);

			// 페이저 윈도우 (5개)
			int window = 5;
			int startPage = Math.max(1, page - window / 2);
			int endPage = Math.min(totalPages, startPage + window - 1);
			startPage = Math.max(1, endPage - window + 1);

			req.setAttribute("products", pageList);
			req.setAttribute("page", page);
			req.setAttribute("size", size);
			req.setAttribute("totalPages", totalPages);
			req.setAttribute("hasPrev", page > 1);
			req.setAttribute("hasNext", page < totalPages);
			req.setAttribute("prevPage", Math.max(1, page - 1));
			req.setAttribute("nextPage", Math.min(totalPages, page + 1));
			req.setAttribute("startPage", startPage);
			req.setAttribute("endPage", endPage);
		}

		if ("giftshop".equals(key) && "detail".equals(method)) {
			long pid = parseLong(req.getParameter("productId"), -1L);
			List<ProductView> all = buildProducts();
			ProductView found = null;
			for (ProductView pv : all) {
				if (pv.getProductId() == pid) {
					found = pv;
					break;
				}
			}
			req.setAttribute("product", found);
		}

		if ("rightSidebar".equals(key) && "view".equals(method)) {
			ProfileBox profile = new ProfileBox("testuser123", 3, 1240, 2000, 74);


			req.setAttribute("profile", profile);
		}

	}

	private long parseLong(String s, long def) {
		try {
			return Long.parseLong(s);
		} catch (Exception e) {
			return def;
		}
	}

	private int parseInt(String s, int def) {
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			return def;
		}
	}

	public static class WorkspaceView {
		private final Long id;
		private final String name;

		public WorkspaceView(Long id, String name) {
			this.id = id;
			this.name = name;
		}

		public Long getId() {
			return id;
		}

		public String getName() {
			return name;
		}
	}

	public static class NoticeView {
		private final long id;
		private final String title;
		private final String content;

		public NoticeView(long id, String title, String content) {
			this.id = id;
			this.title = title;
			this.content = content;
		}

		public long getId() {
			return id;
		}

		public String getTitle() {
			return title;
		}

		public String getContent() {
			return content;
		}
	}

	public static class ProblemCard {
		private final long id;
		private final String title;
		private final String difficulty;
		private final String difficultyClass;
		private final boolean submitted;
		private final String deadline;
		private final int submitCount;
		private final List<String> tags;

		public ProblemCard(long id, String title, String difficulty, String difficultyClass,
			boolean submitted, String deadline, int submitCount, List<String> tags) {
			this.id = id;
			this.title = title;
			this.difficulty = difficulty;
			this.difficultyClass = difficultyClass;
			this.submitted = submitted;
			this.deadline = deadline;
			this.submitCount = submitCount;
			this.tags = tags;
		}

		public long getId() {
			return id;
		}

		public String getTitle() {
			return title;
		}

		public String getDifficulty() {
			return difficulty;
		}

		public String getDifficultyClass() {
			return difficultyClass;
		}

		public boolean isSubmitted() {
			return submitted;
		}

		public String getDeadline() {
			return deadline;
		}

		public int getSubmitCount() {
			return submitCount;
		}

		public List<String> getTags() {
			return tags;
		}
	}

	public static class ProductView {
		private final long productId;
		private final String brand;
		private final String name;
		private final int price;
		private final String img;

		public ProductView(long productId, String brand, String name, int price, String img) {
			this.productId = productId;
			this.brand = brand;
			this.name = name;
			this.price = price;
			this.img = img;
		}

		public long getProductId() {
			return productId;
		}

		public String getBrand() {
			return brand;
		}

		public String getName() {
			return name;
		}

		public int getPrice() {
			return price;
		}

		public String getImg() {
			return img;
		}
	}

	private List<ProductView> buildProducts() {
		List<ProductView> list = new ArrayList<>();
		// 예시 JSON 매핑 (productId = 1..12)
		list.add(new ProductView(1, "할리스", "할리스콘", 6400, ""));
		list.add(new ProductView(2, "빽다방", "카페라떼(ICED)", 3200, ""));
		list.add(new ProductView(3, "파스쿠찌", "모바일 금액권 5만원", 50000, ""));
		list.add(new ProductView(4, "브랜드", "상품명 예시1", 9900, ""));
		list.add(new ProductView(5, "브랜드", "상품명 예시2", 9900, ""));
		list.add(new ProductView(6, "브랜드", "상품명 예시3", 9900, ""));
		list.add(new ProductView(7, "브랜드", "상품명 예시4", 9900, ""));
		list.add(new ProductView(8, "브랜드", "상품명 예시5", 12320, ""));
		list.add(new ProductView(9, "브랜드", "상품명 예시6", 12320, ""));
		list.add(new ProductView(10, "브랜드", "상품명 예시7", 12320, ""));
		list.add(new ProductView(11, "브랜드", "상품명 예시8", 12320, ""));
		list.add(new ProductView(12, "브랜드", "상품명 예시9", 13400, ""));

		// 더미로 60개까지 확장 (스타일 테스트용)
		if (list.size() < 60) {
			int base = list.size() + 1;
			for (int i = base; i <= 60; i++) {
				String brand = (i % 3 == 0) ? "할리스" : (i % 3 == 1 ? "빽다방" : "파스쿠찌");
				String name = brand + " 상품 " + i;
				int price = 3000 + (i % 10) * 700;
				list.add(new ProductView(i, brand, name, price, ""));
			}
		}
		return list;
	}

	public static class ProfileBox {
		private final String nickname;
		private final int level;
		private final int exp;
		private final int expMax;
		private final int accuracy;

		public ProfileBox(String nickname, int level, int exp, int expMax, int accuracy) {
			this.nickname = nickname; this.level = level; this.exp = exp; this.expMax = expMax;
			this.accuracy = accuracy;
		}
		public String getNickname() { return nickname; }
		public int getLevel() { return level; }
		public int getExp() { return exp; }
		public int getExpMax() { return expMax; }
		public int getAccuracy() { return accuracy; }
		public int getExpPercent() {
			if (expMax <= 0) return 0;
			return Math.min(100, (int)Math.round(exp * 100.0 / expMax));
		}
	}

}
