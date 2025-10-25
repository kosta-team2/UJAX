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

	/**
	 * 사용자(구매자) 리워드 조회
	 */
	Long getMemberReward(Long userId);

	/**
	 * 기프티콘 결제
	 */
	void confirmPayment(Long userId, Long productId);

}
