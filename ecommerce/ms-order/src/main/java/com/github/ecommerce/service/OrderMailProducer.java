package com.github.ecommerce.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.github.ecommerce.dto.OrderMailDto;

@Service
public class OrderMailProducer {

	@Value("${topic.name.producer}")
	private String orderTopic;

	private final KafkaTemplate<String, OrderMailDto> kafkaTemplate;

	private static final Logger log = LoggerFactory.getLogger(OrderMailProducer.class);

	public OrderMailProducer(KafkaTemplate<String, OrderMailDto> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void sendMessage(OrderMailDto orderMail, Long order) {
		try {
			kafkaTemplate.send(orderTopic, orderMail);
			log.info("Pedido {} enviado com sucesso para o tópico {}", order, orderTopic);
		} catch (Exception e) {
			log.error("Erro ao enviar pedido {} para o tópico {}", order, orderTopic);
		}
	}

}
