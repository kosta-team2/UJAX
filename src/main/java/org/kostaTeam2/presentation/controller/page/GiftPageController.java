package org.kostaTeam2.presentation.controller.page;

import org.kostaTeam2.application.service.GiftService;
import org.kostaTeam2.domain.gift.Gift;
import org.kostaTeam2.dto.response.GiftPage;
import org.kostaTeam2.global.exception.BadRequestException;
import org.kostaTeam2.presentation.controller.dto.SessionUser;
import org.kostaTeam2.presentation.view.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GiftPageController implements Controller {
	private final GiftService giftService;

	public GiftPageController(GiftService giftService) {
		this.giftService = giftService;
	}

	@Override
	public ModelAndView handle(String methodName, HttpServletRequest request, HttpServletResponse response) throws
		Exception {
		return switch (methodName) {
			case "showGift" -> showGiftItem(request, response);
			case "showGiftPage" -> showGiftPage(request, response);
			case "checkMemberReward" -> getMemberReward(request, response);
			case "giftPayment" -> confirmPayment(request, response);
			default -> throw new BadRequestException("workspace methodName이 올바르지 않습니다.");
		};
	}

	private ModelAndView showGiftItem(HttpServletRequest request, HttpServletResponse response) {
		Long productId;
		try {
			productId = Long.parseLong(request.getParameter("productId"));
		} catch (NumberFormatException e) {
			throw new BadRequestException("잘못된 productId 값을 요청 하셨습니다.");
		}

		Gift gift = giftService.getGiftInfo(productId);
		request.setAttribute("productId", productId);
		request.setAttribute("productName", gift.getProductName());
		request.setAttribute("productPrice", gift.getProductPrice());
		request.setAttribute("productImage", gift.getProductImage());

		String target = request.getContextPath() + "/workspace/giftshop-detail.jsp";
		return new ModelAndView(target);
	}

	private ModelAndView showGiftPage(HttpServletRequest request, HttpServletResponse response) {
		int page, size;
		try {
			page = Integer.valueOf(request.getParameter("page"));
			size = Integer.valueOf(request.getParameter("size"));
		} catch (NumberFormatException e) {
			throw new BadRequestException("잘못된 page, size 값을 요청 하셨습니다.");
		}

		GiftPage giftPage = giftService.getPageGiftInfo(page, size);

		request.setAttribute("products", giftPage.getGifts());
		request.setAttribute("page", giftPage.getPage());
		request.setAttribute("size", giftPage.getSize());
		request.setAttribute("totalPages", giftPage.getTotalPages());
		request.setAttribute("hasPrev", giftPage.isHasPrev());
		request.setAttribute("hasNext", giftPage.isHasNext());
		request.setAttribute("prevPage", giftPage.getPrevPage());
		request.setAttribute("nextPage", giftPage.getNextPage());
		request.setAttribute("startPage", giftPage.getStartPage());
		request.setAttribute("endPage", giftPage.getEndPage());

		String target = request.getContextPath() + "/workspace/giftshop.jsp";
		return new ModelAndView(target);
	}

	private ModelAndView getMemberReward(HttpServletRequest request, HttpServletResponse response) {
		SessionUser sessionUser = (SessionUser)request.getSession().getAttribute("SessionUser");
		Long userId = sessionUser.memberId();

		Long reward = giftService.getMemberReward(userId);
		request.setAttribute("userReward", reward);

		String target = request.getContextPath() + "/workspace/giftshop.jsp";
		return new ModelAndView(target);
	}

	private ModelAndView confirmPayment(HttpServletRequest request, HttpServletResponse response) {
		SessionUser sessionUser = (SessionUser)request.getSession().getAttribute("SessionUser");
		Long userId = sessionUser.memberId();

		Long productId;
		try {
			productId = Long.parseLong(request.getParameter("productId"));
		} catch (NumberFormatException e) {
			throw new BadRequestException("잘못된 productId 값을 요청 하셨습니다.");
		}

		giftService.confirmPayment(userId, productId);

		String target = request.getContextPath() + "/workspace/giftshop.jsp";
		return new ModelAndView(target);
	}
}
