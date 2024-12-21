package com.resilience4j.resililence4j.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
@Slf4j
public class RateLimiterController {

    private final AtomicInteger atomicInteger=new AtomicInteger(0);

    @GetMapping("/getMessage")
    @RateLimiter(name = "rateLimit", fallbackMethod = "rateLimiterFallback")
    public ResponseEntity<String> getMessage(@RequestParam(value = "name",defaultValue = "Deepak")String name){
        int requestNumber=atomicInteger.incrementAndGet();

        log.info("Processing Request for: {}, Request Number: {}", name, requestNumber);

        return ResponseEntity.ok().body("Hello from Rate Limiting: "+name);
    }

    public ResponseEntity<String> rateLimiterFallback(@RequestParam(value = "name", defaultValue = "Deepak") String name, Exception e){

        int requestNumber=atomicInteger.get();

        log.warn("Rate limit fallback triggered for name :{}, Error:{}, Request Number: {}",name, e.getMessage(),requestNumber);

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate Limit Exceeded: "
                + name);
    }

}
