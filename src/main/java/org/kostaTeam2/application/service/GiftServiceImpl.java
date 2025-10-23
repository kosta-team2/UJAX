package org.kostaTeam2.application.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.kostaTeam2.domain.gift.Gift;
import org.kostaTeam2.domain.gift.GiftRepository;
import org.kostaTeam2.dto.response.GiftPage;
import org.kostaTeam2.global.exception.DBException;

public class GiftServiceImpl implements GiftService {
	private final DataSource ds;
	private final GiftRepository giftRepository;

	public GiftServiceImpl(DataSource ds, GiftRepository giftRepository) {
		this.ds = ds;
		this.giftRepository = giftRepository;
	}

	@Override
	public Gift getGiftItem(Long giftId) {
		try (Connection conn = ds.getConnection()) {

			return giftRepository.findById(conn, giftId);
		} catch (SQLException e) {
			throw new DBException("물건 상세 불러오기 DB 에러");
		}
	}

	@Override
	public GiftPage getGiftItemList(int page, int size) {
		try (Connection conn = ds.getConnection()) {
			int total = giftRepository.countByWorkspace(conn);

			int totalPages = (int)Math.ceil(total / (double)size);
			if (totalPages == 0) {
				totalPages = 1;
			}

			page = Math.max(1, Math.min(totalPages, page));
			int offset = (page - 1) * size;

			List<Gift> list = giftRepository.findPages(conn, offset, size);

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
			throw new DBException("물건 리스트 불러오기 DB 에러");
		}
	}
}
