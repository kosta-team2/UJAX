package org.kostaTeam2.application.service.workspace;

import org.kostaTeam2.dto.request.NoticeRequest;
import org.kostaTeam2.dto.response.NoticePage;

public interface NoticeService {
	void create(NoticeRequest dto);

	void delete(NoticeRequest dto);

	NoticePage getPaged(NoticeRequest dto);
}
