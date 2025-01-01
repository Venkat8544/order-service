package com.ecom.orderservice.model;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderLine {
	private String productId;
	private int quantity;
	private BigDecimal unitPrice;
	private String productName;
	private String productDescription;
}