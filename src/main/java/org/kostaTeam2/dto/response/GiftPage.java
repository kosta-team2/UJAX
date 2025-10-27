package org.kostaTeam2.dto.response;

import java.util.List;

import org.kostaTeam2.domain.gift.Gift;

public class GiftPage {
	private final List<Gift> gifts;
	private final int page;
	private final int size;
	private final int totalPages;
	private final boolean hasPrev;
	private final boolean hasNext;
	private final int prevPage;
	private final int nextPage;
	private final int startPage;
	private final int endPage;

	public GiftPage(List<Gift> gifts, int page, int size, int totalPages, boolean hasPrev, boolean hasNext,
		int prevPage, int nextPage, int startPage, int endPage) {
		this.gifts = gifts;
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

	public List<Gift> getGifts() {
		return gifts;
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
