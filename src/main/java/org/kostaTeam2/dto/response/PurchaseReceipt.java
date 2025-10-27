package org.kostaTeam2.dto.response;

import java.time.LocalDateTime;

public record PurchaseReceipt(
	long memberId,
	long productId,
	long paidAmount,
	String barcodeImage,
	LocalDateTime createdAt
) {
	public static PurchaseReceipt completeGiftPurchase(long memberId, long productId, long paidAmount,
		String barcodeImage) {
		return new PurchaseReceipt(memberId, productId, paidAmount, barcodeImage, LocalDateTime.now());
	}
}