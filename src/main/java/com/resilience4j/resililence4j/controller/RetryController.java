package com.resilience4j.resililence4j.controller;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
@Slf4j
public class RetryController {

    @Autowired
    private RestTemplate restTemplate;

    private final AtomicInteger atomicInteger=new AtomicInteger(0);

    @GetMapping("/retry")
    @Retry(name = "retryApi", fallbackMethod = "retryFallback")
    public ResponseEntity<String> getMessage(){
        log.info("Retry attempt count:{}",atomicInteger.incrementAndGet());

        ResponseEntity<String> res=restTemplate.getForEntity("http://localhost:8080/dd",String.class);
        log.info("Status Code:{}",res.getStatusCode());

        atomicInteger.set(0);

        return ResponseEntity.ok().body("Hello from Retry: "+ res.getBody());
    }

    public ResponseEntity<String> retryFallback(Exception e){
        log.warn("Retry fallback triggered after{} attempts.Error:{}",atomicInteger.get(),e.getMessage());
        log.info("---RESPONSE FROM FALLBACK METHOD---");
        atomicInteger.set(0);
        return ResponseEntity.status(500).body("SERVICE IS DOWN, PLEASE TRY AFTER SOMETIME");
    }

    @GetMapping("/dd")
    public String testEndpoint(){
        log.info("Test endpoint called - simulating failure");
        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,"Simulated Failure");
    }
}
