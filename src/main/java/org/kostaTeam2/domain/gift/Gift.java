package org.kostaTeam2.domain.gift;

import java.math.BigDecimal;
import java.sql.Blob;
import java.time.LocalDateTime;

public class Gift {
	private Long productId;
	private String productName;
	private BigDecimal productPrice;
	private Blob productImage;
	private LocalDateTime createdAt;

	public Gift(String productName, BigDecimal productPrice, Blob productImage) {
		this.productName = productName;
		this.productPrice = productPrice;
		this.productImage = productImage;
	}

	public Gift(Long productId, String productName, BigDecimal productPrice, Blob productImage) {
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

	public BigDecimal getProductPrice() {
		return productPrice;
	}

	public Blob getProductImage() {
		return productImage;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}
