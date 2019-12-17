package com.japarejo.zipkin.order.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.japarejo.zipkin.order.model.Order;
import com.japarejo.zipkin.order.model.Payment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/orders")
public class OrderController {

	private List<Order> orders=new ArrayList<>();
	
	private String paymentServiceURL="http://localhost:8081/api/payments/";
	
	@Autowired
	private RestTemplateBuilder rtbuilder;	
	
	@GetMapping
	public List<Order> orders(){
		log.info("Returning "+orders.size()+" orders");
		return orders;
	}
	
	@GetMapping(path = "/{id}")
	public Order getOrder(@PathVariable String orderId)
	{
		Order result=null;
		for(Order candidate:orders) {
			if(candidate.getId().equals(orderId))
				result=candidate;
		}
		if(result!=null)
			return result;
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	}
	
	@PostMapping 
	public ResponseEntity<Order> placeOrder(@RequestBody Order neworder) throws URISyntaxException{
		String newOrderId=UUID.randomUUID().toString();				
		Payment payment=new Payment(null,"Payment of order "+newOrderId,neworder.getCost()*neworder.getQuantity());
		log.info("Invoking payment Service");
		RestTemplate template=rtbuilder.build();
		ResponseEntity<Payment> savedPayment=template.postForEntity(paymentServiceURL,payment,Payment.class);
		if(savedPayment.getStatusCode().equals(HttpStatus.CREATED)) {
			log.info("Payment created");
			neworder.setId(newOrderId);
			orders.add(neworder);
		}else {
			log.error("Error invoking payment service (STATUS:"+savedPayment.getStatusCode()+")");
			return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
		}
		URI uri=new URI("http://localhost:8080/api/orders/"+newOrderId);
		return ResponseEntity.created(uri).body(neworder);
	}
}
