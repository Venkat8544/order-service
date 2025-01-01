package com.ecom.orderservice.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ecom.orderservice.model.EcomOrders;

@Repository
public interface EcomOrdersRepository extends MongoRepository<EcomOrders, String> {

	Page<EcomOrders> findAll(Pageable pageable);

}
