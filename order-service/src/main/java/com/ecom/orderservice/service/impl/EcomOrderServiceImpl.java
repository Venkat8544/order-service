package com.ecom.orderservice.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.orderservice.dto.EcomOrdersDto;
import com.ecom.orderservice.dto.OrderCreationResponseDto;
import com.ecom.orderservice.exception.OrderServiceException;
import com.ecom.orderservice.model.EcomOrders;
import com.ecom.orderservice.repository.EcomOrdersRepository;
import com.ecom.orderservice.service.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EcomOrderServiceImpl implements OrderService {

	private final EcomOrdersRepository orderRepo;

	private final MongoTemplate mongoTemplate;

	@Override
	public List<OrderCreationResponseDto> createEcomOrder(List<EcomOrdersDto> orderDto) {
		try {
			List<EcomOrders> orderData = mapDtoToModel(orderDto);
			orderRepo.saveAll(orderData);
			return prepareResponse(orderData);
		} catch (Exception e) {
			throw new OrderServiceException("Failed to create Ecom order: " + e.getMessage());
		}
	}

	private List<OrderCreationResponseDto> prepareResponse(List<EcomOrders> orders) {
		return orders.stream()
				.map(ord -> OrderCreationResponseDto.builder().orderStatus("Created").orderId(ord.getOrderId()).build())
				.collect(Collectors.toList());

	}

	private List<EcomOrders> mapDtoToModel(List<EcomOrdersDto> orderDtos) {
		return orderDtos.stream()
				.map(order -> EcomOrders.builder().orderedDate(order.getOrderedDate()).customerId(order.getCustomerId())
						.orderLines(order.getOrderLines()).orderStatus(order.getOrderStatus())
						.totalAmount(order.getTotalAmount()).shippingAddress(order.getShippingAddress())
						.orderId(order.getOrderId()).build())
				.collect(Collectors.toList());
	}

	@Override
	public List<EcomOrdersDto> getAllOrders(Pageable pageable) {
		Page<EcomOrders> ordersPage = orderRepo.findAll(pageable);
		return ordersPage.stream().map(this::mapOrdertoDto).collect(Collectors.toList());
	}

	private EcomOrdersDto mapOrdertoDto(EcomOrders orderObj) {
		return EcomOrdersDto.builder().orderedDate(orderObj.getOrderedDate()).orderStatus(orderObj.getOrderStatus())
				.shippingAddress(orderObj.getShippingAddress()).totalAmount(orderObj.getTotalAmount())
				.orderId(orderObj.getOrderId()).orderLines(orderObj.getOrderLines())
				.customerId(orderObj.getCustomerId()).id(orderObj.getId()).build();
	}

	@Override
	public EcomOrdersDto findByDocumentId(String id) {
		Optional<EcomOrders> order = orderRepo.findById(id);
		if (order.isPresent()) {
			return mapOrdertoDto(order.get());
		} else {
			throw new OrderServiceException("Order not found with ID: " + id);
		}
	}

	@Override
	public List<EcomOrdersDto> findByProductId(String product) {
		Query query = new Query();
		query.addCriteria(Criteria.where("orderLines.productId").is(product));
		List<EcomOrders> orderList = mongoTemplate.find(query, EcomOrders.class);
		if (orderList.isEmpty()) {
			throw new OrderServiceException("no order found with given product id :" + product);
		} else {
			return orderList.stream().map(this::mapOrdertoDto).collect(Collectors.toList());
		}
	}
}
