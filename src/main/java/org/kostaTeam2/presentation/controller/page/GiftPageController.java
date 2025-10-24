package org.kostaTeam2.presentation.controller.page;

import org.kostaTeam2.application.service.GiftService;
import org.kostaTeam2.domain.gift.Gift;
import org.kostaTeam2.dto.response.GiftPage;
import org.kostaTeam2.global.exception.BadRequestException;
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
			default -> throw new BadRequestException("workspace methodName이 올바르지 않습니다.");
		};
	}

	private ModelAndView showGiftItem(HttpServletRequest request, HttpServletResponse response) {
		Long productId = Long.valueOf(request.getParameter("productId"));

		Gift gift = giftService.getGiftInfo(productId);
		request.setAttribute("productId", productId);
		request.setAttribute("productName", gift.getProductName());
		request.setAttribute("productPrice", gift.getProductPrice());
		request.setAttribute("productImage", gift.getProductImage());

		String target = request.getContextPath() + "/workspace/giftshop-detail.jsp";
		return new ModelAndView(target);
	}

	private ModelAndView showGiftPage(HttpServletRequest request, HttpServletResponse response) {
		int page = Integer.valueOf(request.getParameter("page"));
		int size = Integer.valueOf(request.getParameter("size"));

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

}
