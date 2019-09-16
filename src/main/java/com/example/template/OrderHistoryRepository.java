package com.example.template;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderHistoryRepository extends PagingAndSortingRepository<OrderHistory, Long> {

	List<OrderHistory> findByUserIdOrderByOrderIdDesc(String username);

}
