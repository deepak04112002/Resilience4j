package com.resilience4j.resililence4j.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
@Slf4j
public class CircuitBreakerController {

    private final RestTemplate restTemplate;
    private final AtomicInteger atomicInteger=new AtomicInteger(0);

    public CircuitBreakerController(RestTemplate restTemplate){
        this.restTemplate=restTemplate;
    }

    @GetMapping("/circuitBreaker")
    @CircuitBreaker(name = "circuit", fallbackMethod = "circuitFallback")
    public ResponseEntity<String> getMessage(){
        log.info("Circuit Breaker: Making request to downstream service");

        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:8080/deepak", String.class);

        log.info("Circuit Breaker: Received response from downstream service with status:{}",response.getStatusCode());

        return ResponseEntity.ok().body("Circuit Breaker Response: "+response.getBody());
    }

    public ResponseEntity<String> circuitFallback(Exception e){
        log.warn("Circuit Breaker fallback triggered after {} attempts. Error{}",atomicInteger.get(),e.getMessage());
        log.info("---CIRCUIT BREAKER FALLBACK ACTIVATED---");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Circuit Breaker: Service is Temporarily unavailable");
    }

    @GetMapping("/deepak")
    public ResponseEntity<String> testEndpoint(){
        int currentFailures = atomicInteger.incrementAndGet();
        log.info("Test endpoint called - Attempt #{}", currentFailures);

        if(currentFailures<=3){
            log.info("Simulating failure for attempt #{}",currentFailures);
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,"Simulated Failure # "+currentFailures);
        }

        atomicInteger.set(0);
        log.info("Test endpoint: Returning Success after 3 Failures");
        return ResponseEntity.ok("Service recovered successfully after 3 failures");
    }
}
