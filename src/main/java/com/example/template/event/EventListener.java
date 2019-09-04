package com.example.template.event;

import com.example.template.OrderHistory;
import com.example.template.OrderHistoryRepository;
import com.example.template.User;
import com.example.template.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class EventListener {

    @Autowired
    OrderHistoryRepository orderHistoryRepository;
    @Autowired
    UserRepository userRepository;

    /**
     * 각종 이벤트 발생시 저장을 하는 공간
     * @param message
     * @param consumerRecord
     */
    @KafkaListener(topics = "${eventTopic}")
    public void onMessage(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        AbstractEvent event = null;
        try {
            event = objectMapper.readValue(message, AbstractEvent.class);

            if( event.getEventType().equals(DeliveryCompleted.class.getSimpleName())){
                DeliveryCompleted deliveryCompleted = objectMapper.readValue(message, DeliveryCompleted.class);

                OrderHistory orderHistory = orderHistoryRepository.findById(deliveryCompleted.getOrderId()).get();
                orderHistory.setDeliveryCompleted(true);

                orderHistoryRepository.save(orderHistory);

            }

            else if( event.getEventType().equals(DeliveryStarted.class.getSimpleName())){
                DeliveryStarted deliveryStarted = objectMapper.readValue(message, DeliveryStarted.class);

                OrderHistory orderHistory = orderHistoryRepository.findById(deliveryStarted.getOrderId()).get();
                orderHistory.setDeliveryId(deliveryStarted.getDeliveryId());
                orderHistory.setDeliveryStarted(true);

                orderHistoryRepository.save(orderHistory);

            }

            else if( event.getEventType().equals(OrderPlaced.class.getSimpleName())){
                OrderPlaced orderPlaced = objectMapper.readValue(message, OrderPlaced.class);

                User user = userRepository.findById(orderPlaced.getCustomerName()).get();
                if( user != null ){

                    int payMoney = orderPlaced.getPrice() * orderPlaced.getQuantity();

                    OrderHistory orderHistory = new OrderHistory();
                    orderHistory.setOrderId(orderPlaced.getOrderId());
                    orderHistory.setProductId(orderPlaced.getProductId());
                    orderHistory.setUsername(orderPlaced.getCustomerName());
                    orderHistory.setProductName(orderPlaced.getProductName());
                    orderHistory.setTimestamp(orderPlaced.getTimestamp());
                    orderHistory.setQuantity(orderPlaced.getQuantity());
                    orderHistory.setPayment(payMoney);

                    orderHistoryRepository.save(orderHistory);

                    // 주문 완료시.. (실제로는 결재가 완료 될거 같긴한데..) 잔액 변경

                    user.setMoney( user.getMoney() - payMoney );

                    userRepository.save(user);
                }else{
                    throw new RuntimeException("유저정보가 없습니다.");
                }

            }

//            else if( event.getEventType().equals(DeliveryStarted.class.getSimpleName())){
//
//            }
        } catch (
        IOException e) {
            e.printStackTrace();
        }
    }

}
