package com.github.ecommerce.serializer;

import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ecommerce.dto.OrderMailDto;

public class OrderSerializer implements Serializer<OrderMailDto> {
	
	private static final Logger log = LoggerFactory.getLogger(OrderSerializer.class);

	@Override
	public byte[] serialize(String topic, OrderMailDto order) {
		try {
			return new ObjectMapper().writeValueAsBytes(order);
		} catch (JsonProcessingException e) {
			log.error("Erro ao serializar a mensagem: {}", e.getMessage());
			return null;
		}
	}

}
