package com.ecom.orderservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecom.orderservice.model.EcomOrders;

@Repository
public interface EcomOrdersRepository extends MongoRepository<EcomOrders, String> {

	@Query("{'productId':?0}")
	List<EcomOrders> findOrderByProductId();
	
}
