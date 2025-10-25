package org.kostaTeam2.application.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.kostaTeam2.domain.gift.Gift;
import org.kostaTeam2.domain.gift.GiftRepository;
import org.kostaTeam2.domain.member.Member;
import org.kostaTeam2.domain.member.MemberRepository;
import org.kostaTeam2.dto.response.GiftPage;
import org.kostaTeam2.global.exception.DBException;
import org.kostaTeam2.global.exception.InsufficientBalanceException;
import org.kostaTeam2.global.exception.NotFoundException;

public class
GiftServiceImpl implements GiftService {
	private final DataSource ds;
	private final GiftRepository giftRepository;
	private final MemberRepository memberRepository;

	public GiftServiceImpl(DataSource ds, GiftRepository giftRepository, MemberRepository memberRepository) {
		this.ds = ds;
		this.giftRepository = giftRepository;
		this.memberRepository = memberRepository;
	}

	@Override
	public Gift getGiftInfo(Long giftId) {
		try (Connection conn = ds.getConnection()) {

			Gift gift = giftRepository.findById(conn, giftId);
			if (gift == null) {
				throw new NotFoundException("giftId에 해당하는 상품이 없습니다.");
			}

			return gift;
		} catch (SQLException e) {
			throw new DBException("물건 상세 불러오기 DB 에러가 발생했습니다.");
		}
	}

	@Override
	public GiftPage getPageGiftInfo(int page, int size) {
		try (Connection conn = ds.getConnection()) {
			int total = giftRepository.countProducts(conn);

			int totalPages = (int)Math.ceil(total / (double)size);
			if (totalPages == 0) {
				totalPages = 1;
			}

			page = Math.max(1, Math.min(totalPages, page));
			int offset = (page - 1) * size;

			List<Gift> list = giftRepository.paginationGift(conn, offset, size);

			int window = 5;
			int startPage = Math.max(1, page - window / 2);
			int endPage = Math.min(totalPages, startPage + window - 1);
			startPage = Math.max(1, endPage - window + 1);

			boolean hasPrev = page > 1;
			boolean hasNext = page < totalPages;
			return new GiftPage(
				list, page, size, totalPages,
				hasPrev, hasNext,
				Math.max(1, page - 1),
				Math.min(totalPages, page + 1),
				startPage, endPage
			);
		} catch (SQLException e) {
			throw new DBException("물건 리스트 불러오기 DB 에러가 발생했습니다.");
		}
	}

	@Override
	public Long getMemberReward(Long userId) {
		try (Connection conn = ds.getConnection()) {
			Optional<Member> member = memberRepository.findById(conn, userId);
			if (member.isEmpty()) {
				throw new NotFoundException("사용자가 존재하지 않습니다.");
			}

			return member.get().getReward();
		} catch (SQLException e) {
			throw new DBException("사용자 reward 조회 중 DB 에러가 발생했습니다.");
		}
	}

	@Override
	public void confirmPayment(Long userId, Long productId) {
		try (Connection conn = ds.getConnection()) {
			// 물건 가격 조회
			Gift gift = giftRepository.findById(conn, productId);
			if (gift == null) {
				throw new NotFoundException("상품이 존재하지 않습니다.");
			}

			Long price = gift.getProductPrice();
			if (price == null || price <= 0) {
				throw new IllegalArgumentException("상품 가격에 문제가 있어 구매 불가 합니다.");
			}

			// 결제 프로세스
			int result = memberRepository.debitRewardIfEnough(conn, userId, price);
			if (result != 1) {
				throw new InsufficientBalanceException("잔액이 부족합니다.");
			}

		} catch (InsufficientBalanceException | NotFoundException e) {
			throw e;
		} catch (SQLException e) {
			throw new DBException("기프티콘 결제 중 DB 에러가 발생했습니다.");
		}
	}

}
