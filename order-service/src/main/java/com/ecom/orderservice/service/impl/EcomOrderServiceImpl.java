package com.ecom.orderservice.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
	public OrderCreationResponseDto createEcomOrder(EcomOrdersDto orderDto) {
		try {
			EcomOrders orderData = mapDtoToModel(orderDto);
			orderRepo.save(orderData);
		} catch (Exception e) {
			throw new OrderServiceException("Failed to create Ecom order: " + e.getMessage());
		}
		return OrderCreationResponseDto.builder().orderStatus("Created").orderId(orderDto.getOrderId()).build();
	}

	private EcomOrders mapDtoToModel(EcomOrdersDto orderDto) {
		return EcomOrders.builder().orderedDate(orderDto.getOrderedDate()).customerId(orderDto.getCustomerId())
				.orderLines(orderDto.getOrderLines()).orderStatus(orderDto.getOrderStatus())
				.totalAmount(orderDto.getTotalAmount()).shippingAddress(orderDto.getShippingAddress())
				.orderId(orderDto.getOrderId()).build();
	}

	@Override
	public List<EcomOrdersDto> getAllOrders() {
		List<EcomOrders> orderList = orderRepo.findAll();
		return orderList.stream().map(this::mapOrdertoDto).collect(Collectors.toList());
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
