package org.kostaTeam2.domain.workspace.notice;

public record NoticeTitle(String value) {

	public NoticeTitle {
		if (value == null || value.isBlank())
			throw new IllegalArgumentException("공지사항 제목은 필수 입니다.");
		if (value.length() > 100)
			throw new IllegalArgumentException("공지사항 제목의 길이가 100자를 초과했습니다.");
	}

	public String getValue() {
		return value;
	}

}
