package com.example.template;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface MileageHistoryRepository extends PagingAndSortingRepository<MileageHistory, Long> {

    List<MileageHistory> findByUserIdOrderByIdDesc(String username);
}
