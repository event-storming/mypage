package com.example.template.event;

import com.example.template.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;

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

            /**
             * 배송 완료 이벤트
             */
            if( event.getEventType().equals(DeliveryCompleted.class.getSimpleName())){
                DeliveryCompleted deliveryCompleted = objectMapper.readValue(message, DeliveryCompleted.class);

                OrderHistory orderHistory = orderHistoryRepository.findById(deliveryCompleted.getOrderId()).get();
                orderHistory.setDeliveryCompleted(true);

                orderHistoryRepository.save(orderHistory);

            }
            /**
             * 배송 시작 이벤트
             */
            else if( event.getEventType().equals(DeliveryStarted.class.getSimpleName())){
                DeliveryStarted deliveryStarted = objectMapper.readValue(message, DeliveryStarted.class);

                OrderHistory orderHistory = orderHistoryRepository.findById(deliveryStarted.getOrderId()).get();
                orderHistory.setDeliveryId(deliveryStarted.getDeliveryId());
                orderHistory.setDeliveryStarted(true);

                orderHistoryRepository.save(orderHistory);

            }
            /**
             * 배송 취소 이벤트
             */
            else if( event.getEventType().equals(DeliveryCancelled.class.getSimpleName())){
                DeliveryCancelled deliveryCancelled = objectMapper.readValue(message, DeliveryCancelled.class);

                OrderHistory orderHistory = orderHistoryRepository.findById(deliveryCancelled.getOrderId()).get();
                orderHistory.setDeliveryId(deliveryCancelled.getDeliveryId());
                orderHistory.setDeliveryStarted(false);
                orderHistory.setDeliveryCompleted(false);
                orderHistory.setDeliveryCancelled(true);

                orderHistoryRepository.save(orderHistory);
            }
            /**
             * 주문 생성 이벤트
             */
            else if( event.getEventType().equals(OrderPlaced.class.getSimpleName())) {
                OrderPlaced orderPlaced = objectMapper.readValue(message, OrderPlaced.class);

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
                orderHistory.setOrderState(OrderPlaced.class.getSimpleName());

                orderHistoryRepository.save(orderHistory);

                User user = userRepository.findById(orderPlaced.getCustomerId()).orElse(null);
                if (user == null) {
                    user = new User();
                    user.setUsername(orderPlaced.getCustomerId());
                    user.setNickname(orderPlaced.getCustomerName());
                    user.setAddress(orderPlaced.getCustomerAddr());
                    user.setMileage(0L);

                    userRepository.save(user);
                }
                // 주문 완료시.. 마일리지 적립
                Long mileage = 0L;
                if (user.getMileage() != null) {
                    mileage = user.getMileage() + payMoney / 10;
                } else {
                    mileage = new Long(payMoney / 10);
                }

                user.setMileage(mileage);

                userRepository.save(user);

                MileageHistory mileageHistory = new MileageHistory();
                mileageHistory.setOrderId(orderPlaced.getOrderId());
                mileageHistory.setUserId(orderPlaced.getCustomerId());
                mileageHistory.setTimestamp(orderPlaced.getTimestamp());
                mileageHistory.setMileage(new Long(payMoney / 10));
                mileageHistory.setTotalMileage(mileage);

                mileageHistoryRepository.save(mileageHistory);

            }
            /**
             * 주문 취소 이벤트
             */
            else if( event.getEventType().equals(OrderCancelled.class.getSimpleName())){

                OrderCancelled orderCancelled = objectMapper.readValue(message, OrderCancelled.class);
                int payMoney = orderCancelled.getPrice() * orderCancelled.getQuantity();

                OrderHistory orderHistory = new OrderHistory();
                orderHistory.setOrderId(orderCancelled.getOrderId());
                orderHistory.setProductId(orderCancelled.getProductId());
                orderHistory.setUserId(orderCancelled.getCustomerId());
                orderHistory.setNickName(orderCancelled.getCustomerName());
                orderHistory.setProductName(orderCancelled.getProductName());
                orderHistory.setTimestamp(orderCancelled.getTimestamp());
                orderHistory.setQuantity(orderCancelled.getQuantity());
                orderHistory.setPayment(payMoney);
                orderHistory.setOrderState(OrderCancelled.class.getSimpleName());

                orderHistoryRepository.save(orderHistory);

                User user = userRepository.findById(orderCancelled.getCustomerId()).orElse(null);
                if (user != null) {
                    // 주문 취소시.. 마일리지 회수
                    Long mileage = 0L;
                    if (user.getMileage() != null) {
                        mileage = user.getMileage() - payMoney / 10;
                    } else {
                        mileage = 0L - payMoney / 10;
                    }

                    user.setMileage(mileage);

                    userRepository.save(user);

                    MileageHistory mileageHistory = new MileageHistory();
                    mileageHistory.setOrderId(orderCancelled.getOrderId());
                    mileageHistory.setUserId(orderCancelled.getCustomerId());
                    mileageHistory.setTimestamp(orderCancelled.getTimestamp());
                    mileageHistory.setMileage(new Long(payMoney / 10));
                    mileageHistory.setTotalMileage(mileage);

                    mileageHistoryRepository.save(mileageHistory);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
