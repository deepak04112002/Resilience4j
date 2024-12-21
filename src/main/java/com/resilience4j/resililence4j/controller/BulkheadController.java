package com.resilience4j.resililence4j.controller;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
@Slf4j
public class BulkheadController {

    private final AtomicInteger atomicInteger=new AtomicInteger(0);

    @GetMapping("/bulkhead")
    @Bulkhead(name="bulkhead",fallbackMethod = "bulkheadFallback")
    public ResponseEntity<String> getMessage(@RequestParam(value="name",defaultValue = "Deepak")String name){
        try{
            int currentCalls=atomicInteger.incrementAndGet();
            log.info("BulkHead: Processing Request for: {},Current concurrentCalls:{}",name,currentCalls);

            Thread.sleep(10000);

            log.info("Request Processed for: {}, Current concurrentCalls:{}", name, currentCalls);

            return ResponseEntity.ok(String.format("BulkHead: Request Processed successfully for %s",name));
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            throw new RuntimeException("Request processing interrupted", e);
        }finally {
            atomicInteger.decrementAndGet();
        }
    }

    public ResponseEntity<String> bulkheadFallback(String name, Exception e){
        log.warn("Bulkhead Limit Reached. Concurrent calls:{},Error:{}",atomicInteger.get(),e.getMessage());

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Service Unavailable"+"Please Try Again Later.");
    }
}
