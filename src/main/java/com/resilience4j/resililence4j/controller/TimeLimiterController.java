package com.resilience4j.resililence4j.controller;

import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
public class TimeLimiterController {

    @GetMapping("/timelimiter")
    @TimeLimiter(name = "timelimiter",fallbackMethod = "fallbackResponse")
    public CompletableFuture<String> getMessage(){
        return CompletableFuture.supplyAsync(this::getResponse);
    }

    private String getResponse() {

        if(Math.random()<0.4){
            log.info("Executing within time limit");
            return "Executing Within the time Limit...";
        }else{
            try{
                log.info("Simulating delayed Execution");
                Thread.sleep(10000);
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
                log.error("Operation interrupted:{}",e.getMessage());
                throw new RuntimeException("Operation Interrupted",e);
            }
        }

        return "Operation completed after delay";
    }

    private CompletableFuture<String> fallbackResponse(Throwable t) {
        log.warn("Fallback triggered due to: {}", t.getMessage());
        return CompletableFuture.completedFuture("Service temporarily unavailable. Please try again later.");
    }
}
