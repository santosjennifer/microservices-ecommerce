package com.github.ecommerce.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.github.ecommerce.dto.OrderMailDto;
import com.github.ecommerce.model.EmailMessage;

@Service
public class OrderMailConsumer {
	
    @Autowired
    private EmailService emailService;

    private static final Logger log = LoggerFactory.getLogger(OrderMailConsumer.class); 
    
	@KafkaListener(topics = "${topic.name.consumer}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ConsumerRecord<String, OrderMailDto> payload){
		try {
			String email = payload.value().getEmail();
			String subject = payload.value().getSubject();
			String message = payload.value().getMessage();
			
			if(email != null) {
				EmailMessage emailMessage = new EmailMessage(subject, message, email);
				emailService.send(emailMessage);
			} else {
				log.error("E-mail inválido.");
			}
			
		} catch (Exception e) {
			log.error("Erro ao receber dados do pedido para envio do e-mail: {} ", e.getMessage());
		}
    }
    
}
