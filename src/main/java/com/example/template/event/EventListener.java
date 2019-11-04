package com.example.template.event;

import com.example.template.*;
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
    MileageHistoryRepository mileageHistoryRepository;

    @Autowired
    UserRepository userRepository;

    /**
     * 각종 이벤트 발생시 저장을 하는 공간
     * @param message
     * @param consumerRecord
     */
    @KafkaListener(topics = "${eventTopic}")
    public void onMessage(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {
        System.out.println(message);
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

                User user = userRepository.findById(orderPlaced.getCustomerId()).get();
                if( user == null ) {
                    user.setUsername(orderPlaced.getCustomerId());
                    user.setNickname(orderPlaced.getCustomerName());
                    user.setAddress(orderPlaced.getCustomerAddr());
                    user.setMileage(0L);

                    userRepository.save(user);
                }

                int payMoney = orderPlaced.getPrice() * orderPlaced.getQuantity();

                OrderHistory orderHistory = new OrderHistory();
                orderHistory.setOrderId(orderPlaced.getOrderId());
                orderHistory.setProductId(orderPlaced.getProductId());
                orderHistory.setUserId(orderPlaced.getCustomerId());
                orderHistory.setNickName(orderPlaced.getCustomerName());
                orderHistory.setProductName(orderPlaced.getProductName());
                orderHistory.setTimestamp(orderPlaced.getTimestamp());
                orderHistory.setQuantity(orderPlaced.getQuantity());
                orderHistory.setPayment(payMoney);

                orderHistoryRepository.save(orderHistory);

                // 주문 완료시.. 마일리지 적립
                Long mileage = 0L;
                if( user.getMileage() != null ){
                    mileage = user.getMileage() + payMoney / 10;
                }else{
                    mileage = new Long(payMoney / 10);
                }

                user.setMileage( mileage );

                userRepository.save(user);

                MileageHistory mileageHistory = new MileageHistory();
                mileageHistory.setOrderId(orderPlaced.getOrderId());
                mileageHistory.setUserId(orderPlaced.getCustomerId());
                mileageHistory.setTimestamp(orderPlaced.getTimestamp());
                mileageHistory.setMileage(new Long(payMoney / 10));
                mileageHistory.setTotalMileage(mileage);

                mileageHistoryRepository.save(mileageHistory);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
