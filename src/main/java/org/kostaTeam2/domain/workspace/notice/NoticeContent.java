package org.kostaTeam2.domain.workspace.notice;

public record NoticeContent(String value) {
	public NoticeContent {
		if (value == null || value.isBlank())
			throw new IllegalArgumentException("공지사항 내용은 필수입니다.");
		if (value.length() > 200)
			throw new IllegalArgumentException("공지사항 내용이 200자를 초과했습니다.");
	}

	public String getValue() {
		return value;
	}

}
