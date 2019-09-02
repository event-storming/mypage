package com.example.template.event;

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

@Service
public class EventListener {

    @KafkaListener(topics = "${eventTopic}")
    public void onMessage(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        AbstractEvent event = null;
        try {
            event = objectMapper.readValue(message, AbstractEvent.class);

            if( event.getEventType().equals(DeliveryCompleted.class.getSimpleName())){

            }

            else if( event.getEventType().equals(DeliveryStarted.class.getSimpleName())){

            }

            else if( event.getEventType().equals(OrderPlaced.class.getSimpleName())){

            }

            else if( event.getEventType().equals(DeliveryStarted.class.getSimpleName())){

            }
        } catch (
        IOException e) {
            e.printStackTrace();
        }
    }

}
