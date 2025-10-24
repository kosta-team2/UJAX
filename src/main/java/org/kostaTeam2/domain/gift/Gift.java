package org.kostaTeam2.domain.gift;

import java.time.LocalDateTime;

public class Gift {
	private Long productId;
	private String productName;
	private Long productPrice;
	private String productImage;
	private LocalDateTime createdAt;

	public Gift(String productName, Long productPrice, String productImage) {
		this.productName = productName;
		this.productPrice = productPrice;
		this.productImage = productImage;
	}

	public Gift(Long productId, String productName, Long productPrice, String productImage) {
		this.productId = productId;
		this.productName = productName;
		this.productPrice = productPrice;
		this.productImage = productImage;
	}

	public Long getProductId() {
		return productId;
	}

	public String getProductName() {
		return productName;
	}

	public Long getProductPrice() {
		return productPrice;
	}

	public String getProductImage() {
		return productImage;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}
