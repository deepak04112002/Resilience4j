package com.resilience4j.resililence4j.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CacheController {

    @Value("${cache.enabled:true}")
    private boolean cacheEnabled;

    @GetMapping("/cached")
    @Cacheable(value = "myCache",condition = "#root.target.cacheEnabled")
    public String getCacheValue(){
        try{
            Thread.sleep(5000);
            log.info("Now Cached u will get response faster now");
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
        return "This response is cached";
    }

    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

}
