package org.kostaTeam2.dto.response;

import java.util.List;

public class CommentPageResponse {
	private final List<Item> items;
	private final int page;
	private final int pageSize;
	private final int total;

	public CommentPageResponse(List<Item> items, int page, int pageSize, int total) {
		this.items = items;
		this.page = page;
		this.pageSize = pageSize;
		this.total = total;
	}

	public List<Item> getItems() {
		return items;
	}

	public int getPage() {
		return page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getTotal() {
		return total;
	}

	public static class Item {
		private final long id;
		private final String text;
		private final String ts;
		private final boolean isOwner;
		private final User user;

		public Item(long id, String text, String ts, boolean isOwner, User user) {
			this.id = id;
			this.text = text;
			this.ts = ts;
			this.isOwner = isOwner;
			this.user = user;
		}

		public long getId() {
			return id;
		}

		public String getText() {
			return text;
		}

		public String getTs() {
			return ts;
		}

		public boolean getIsOwner() {
			return isOwner;
		}

		public User getUser() {
			return user;
		}
	}

	public static class User {
		private final long id;
		private final String name;

		public User(long id, String name) {
			this.id = id;
			this.name = name;
		}

		public long getId() {
			return id;
		}

		public String getName() {
			return name;
		}
	}
}
