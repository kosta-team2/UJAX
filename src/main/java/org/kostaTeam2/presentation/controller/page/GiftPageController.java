package org.kostaTeam2.presentation.controller.page;

import org.kostaTeam2.application.service.GiftService;
import org.kostaTeam2.domain.gift.Gift;
import org.kostaTeam2.dto.response.GiftPage;
import org.kostaTeam2.dto.response.PurchaseReceipt;
import org.kostaTeam2.global.exception.BadRequestException;
import org.kostaTeam2.infrastructure.mail.MailService;
import org.kostaTeam2.presentation.controller.dto.SessionUser;
import org.kostaTeam2.presentation.view.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GiftPageController implements Controller {
	private final GiftService giftService;
	private final MailService mailService;

	public GiftPageController(GiftService giftService, MailService mailService) {
		this.giftService = giftService;
		this.mailService = mailService;
	}

	@Override
	public ModelAndView handle(String methodName, HttpServletRequest request, HttpServletResponse response) throws
		Exception {
		return switch (methodName) {
			case "showGift" -> showProductInfo(request, response);
			case "showGiftPage" -> showProductPaging(request, response);
			case "checkMemberReward" -> checkCredit(request, response);
			case "confirmPayment" -> confirmPayment(request, response);
			default -> throw new BadRequestException("methodName이 올바르지 않습니다.");
		};
	}

	private ModelAndView showProductInfo(HttpServletRequest request, HttpServletResponse response) {
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

		String target = request.getContextPath() + "/giftshop/giftshop-detail.jsp";
		return new ModelAndView(target);
	}

	private ModelAndView showProductPaging(HttpServletRequest request, HttpServletResponse response) {
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

		String target = request.getContextPath() + "/giftshop/giftshop.jsp";
		return new ModelAndView(target);
	}

	private ModelAndView checkCredit(HttpServletRequest request, HttpServletResponse response) {
		SessionUser sessionUser = (SessionUser)request.getSession().getAttribute("SessionUser");
		Long userId = sessionUser.memberId();

		Long productId;
		try {
			productId = Long.parseLong(request.getParameter("productId"));
		} catch (NumberFormatException e) {
			throw new BadRequestException("잘못된 productId 값을 요청 하셨습니다.");
		}

		Long productPrice;
		try {
			productPrice = Long.parseLong(request.getParameter("productPrice"));
		} catch (NumberFormatException e) {
			throw new BadRequestException("잘못된 productPrice 값을 받았습니다.");
		}

		Long reward = giftService.getMemberReward(userId);
		request.setAttribute("userReward", reward);
		request.setAttribute("productId", productId);
		request.setAttribute("productPrice", productPrice);
		request.setAttribute("remain", reward - productPrice);

		String target = request.getContextPath() + "/giftshop/check-order.jsp";
		return new ModelAndView(target);
	}

	private ModelAndView confirmPayment(HttpServletRequest request, HttpServletResponse response) {
		SessionUser sessionUser = (SessionUser)request.getSession().getAttribute("SessionUser");
		Long userId = sessionUser.memberId();
		String userEmail = sessionUser.email();
		String userNickname = sessionUser.nickname();

		Long productId;
		try {
			productId = Long.parseLong(request.getParameter("productId"));
		} catch (NumberFormatException e) {
			throw new BadRequestException("잘못된 productId 값을 요청 하셨습니다.");
		}

		Long productPrice;
		try {
			productPrice = Long.parseLong(request.getParameter("productPrice"));
		} catch (NumberFormatException e) {
			throw new BadRequestException("잘못된 productPrice 값을 받았습니다.");
		}

		Long remain;
		try {
			remain = Long.parseLong(request.getParameter("remain"));
		} catch (NumberFormatException e) {
			throw new BadRequestException("잘못된 remain 값을 받았습니다.");
		}

		request.setAttribute("productPrice", productPrice);
		request.setAttribute("remain", remain);

		PurchaseReceipt receipt = giftService.confirmPayment(userId, productId);

		mailService.sendMail(userEmail, "[UJAX] 구매하신 기프티콘입니다.", receipt.barcodeImage());

		String target = request.getContextPath() + "/giftshop/order.jsp";
		return new ModelAndView(target);
	}

}
