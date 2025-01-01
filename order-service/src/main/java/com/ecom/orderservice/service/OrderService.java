package com.ecom.orderservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecom.orderservice.dto.EcomOrdersDto;
import com.ecom.orderservice.dto.OrderCreationResponseDto;

@Service
public interface OrderService {
	OrderCreationResponseDto createEcomOrder(EcomOrdersDto orderDto);

	List<EcomOrdersDto> getAllOrders();

	EcomOrdersDto findByDocumentId(String id);

	List<EcomOrdersDto> findByProductId(String product);
}
