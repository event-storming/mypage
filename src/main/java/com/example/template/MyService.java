package com.example.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyService {

    @Autowired
    OrderHistoryRepository orderHistoryRepository;

    @Autowired
    MileageHistoryRepository mileageHistoryRepository;

    public List<OrderHistory> myOrderHistory(String userId){
        return this.orderHistoryRepository.findByUserIdOrderByOrderIdDesc(userId);
    }

    public List<MileageHistory> myMileageHistory(String userId){
        return this.mileageHistoryRepository.findByUserIdOrderByIdDesc(userId);
    }
}
