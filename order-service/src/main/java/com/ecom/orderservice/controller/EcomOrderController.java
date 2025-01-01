package com.ecom.orderservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.orderservice.dto.EcomOrdersDto;
import com.ecom.orderservice.dto.OrderCreationResponseDto;
import com.ecom.orderservice.service.impl.EcomOrderServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ecom")
@RequiredArgsConstructor
public class EcomOrderController {

	private final EcomOrderServiceImpl orderServiceImpl;

	@PostMapping("/order")
	public ResponseEntity<OrderCreationResponseDto> createEcomOrder(@Valid @RequestBody EcomOrdersDto orderRequest) {
		OrderCreationResponseDto status = orderServiceImpl.createEcomOrder(orderRequest);
		return new ResponseEntity<>(status, HttpStatus.CREATED);
	}

	@GetMapping("/orders")
	public ResponseEntity<List<EcomOrdersDto>> getAllOrders() {
		List<EcomOrdersDto> orders = orderServiceImpl.getAllOrders();
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<EcomOrdersDto> getOrderById(@PathVariable String id) {
		EcomOrdersDto orders = orderServiceImpl.findByDocumentId(id);
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}

	@GetMapping("/orders/product")
	public ResponseEntity<List<EcomOrdersDto>> getOrdersByProductId(@RequestParam String productId) {
		List<EcomOrdersDto> orders = orderServiceImpl.findByProductId(productId);
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}

}
