package org.kostaTeam2.dto.response;

import java.util.List;

import org.kostaTeam2.domain.workspace.notice.Notice;

public class NoticePage {
	List<Notice> notices;
	int page;
	int size;
	int totalPages;
	boolean hasPrev;
	boolean hasNext;
	int prevPage;
	int nextPage;
	int startPage;
	int endPage;

	public NoticePage(List<Notice> notices, int page, int size, int totalPages, boolean hasPrev, boolean hasNext,
		int prevPage, int nextPage, int startPage, int endPage) {
		this.notices = notices;
		this.page = page;
		this.size = size;
		this.totalPages = totalPages;
		this.hasPrev = hasPrev;
		this.hasNext = hasNext;
		this.prevPage = prevPage;
		this.nextPage = nextPage;
		this.startPage = startPage;
		this.endPage = endPage;
	}

	public List<Notice> getNotices() {
		return notices;
	}

	public int getPage() {
		return page;
	}

	public int getSize() {
		return size;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public boolean isHasPrev() {
		return hasPrev;
	}

	public boolean isHasNext() {
		return hasNext;
	}

	public int getPrevPage() {
		return prevPage;
	}

	public int getNextPage() {
		return nextPage;
	}

	public int getStartPage() {
		return startPage;
	}

	public int getEndPage() {
		return endPage;
	}
}
