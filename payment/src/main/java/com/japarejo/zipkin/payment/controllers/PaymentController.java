package com.japarejo.zipkin.payment.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

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

import com.japarejo.zipkin.payment.model.Payment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

	private List<Payment> payments=new ArrayList<>();
		
	
	private RestTemplate template=new RestTemplate();
	
	@GetMapping
	public List<Payment> Payments(){
		log.info("Returning "+payments.size()+" Payments");
		return payments;
	}
	
	@GetMapping(path = "/{id}")
	public Payment getPayment(@PathVariable String paymentId)
	{
		Payment result=null;
		for(Payment candidate:payments) {
			if(candidate.getId().equals(paymentId))
				result=candidate;
		}
		if(result!=null)
			return result;
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	}
	
	@PostMapping 
	public ResponseEntity<Payment> placePayment(@RequestBody Payment newPayment, HttpServletRequest request) throws URISyntaxException{
		String newPaymentId=UUID.randomUUID().toString();						
		newPayment.setId(newPaymentId);
		payments.add(newPayment);		
		URI uri=new URI("http://localhost:8080/api/payments/"+newPaymentId);
		log.info("Payment created!");
		return ResponseEntity.created(uri).body(newPayment);
	}
}

