package org.kostaTeam2.application.service;

import org.kostaTeam2.domain.gift.Gift;
import org.kostaTeam2.dto.response.GiftPage;

public interface GiftService {

	/**
	 * 물품 상세 보기
	 */
	Gift getGiftInfo(Long giftId);

	/**
	 * 물품 리스트 보기
	 */
	GiftPage getPageGiftInfo(int page, int size);
}
