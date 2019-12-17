package com.japarejo.zipkin.payment.model;

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
