package com.ecom.orderservice.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderCreationResponseDto {
	String orderId;
	String orderStatus;
}
