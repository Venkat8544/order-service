package com.ecom.orderservice.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ecom.orderservice.dto.EcomOrdersDto;
import com.ecom.orderservice.dto.OrderCreationResponseDto;

@Service
public interface OrderService {
	List<OrderCreationResponseDto> createEcomOrder(List<EcomOrdersDto> orderDto);

	List<EcomOrdersDto> getAllOrders(Pageable pageable);

	EcomOrdersDto findByDocumentId(String id);

	List<EcomOrdersDto> findByProductId(String product);
}
