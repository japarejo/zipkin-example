package com.japarejo.zipkin.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data	
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
	String id;
	String concept;
	double amount;
}
