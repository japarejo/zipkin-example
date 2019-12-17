package com.japarejo.zipkin.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
	String id;
	String product;
	long quantity;
	double cost;
	String currency; 
}
