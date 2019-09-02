package com.example.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyService {

    @Autowired
    OrderHistoryRepository orderHistoryRepository;

    public List<OrderHistory> myOrderHistory(String username){
        return this.orderHistoryRepository.findByUsername(username);
    }
}
