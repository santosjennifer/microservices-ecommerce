package com.github.ecommerce.deserializer;

import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ecommerce.dto.OrderMailDto;

public class OrderMailDeserializer implements Deserializer<OrderMailDto> {
	
	private static final Logger log = LoggerFactory.getLogger(OrderMailDeserializer.class);

	@Override
	public OrderMailDto deserialize(String topic, byte[] order) {
		try {
			return new ObjectMapper().readValue(order, OrderMailDto.class);
		} catch (Exception e) {
			log.error("Erro ao desserializar a mensagem: {}", e.getMessage());
			return null;
		}	
	}

}
