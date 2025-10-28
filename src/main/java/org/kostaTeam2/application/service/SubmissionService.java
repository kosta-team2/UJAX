package org.kostaTeam2.application.service;

import org.kostaTeam2.dto.request.SubmissionIngestRequest;

public interface SubmissionService {
	void ingest(Long memberId, SubmissionIngestRequest dto);
}
