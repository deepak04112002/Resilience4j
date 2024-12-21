# Resilience4j Spring Boot Demo

## Overview
This project demonstrates the implementation of fault tolerance patterns using Resilience4j with Spring Boot. It includes examples of Rate Limiting, Circuit Breaking, Bulkhead, Retry, and Time Limiter patterns.

## Features
- Rate Limiting: Controls the rate of incoming requests
- Circuit Breaking: Prevents cascading failures
- Bulkhead: Isolates different service calls
- Retry: Automatically retries failed operations
- Time Limiter: Sets timeout for operations
- Caching: Implements caching using Caffeine

## Prerequisites
- Java 17 or higher
- Maven 3.6+
- Spring Boot 3.x

## Getting Started

### Installation
1. Clone the repository
```bash
git clone [repository-url]
cd resilience4j-demo
```
2. Build the project
```bash
mvn clean install
```

3. Run the application
```bash
mvn spring-boot:run
```

### API Endpoints

#### Rate Limiter Example
```bash
curl 'http://localhost:8080/getMessage?name=user1'
```
- Limits: 1 request per 10 seconds

#### Retry Example
```bash
curl 'http://localhost:8080/test?throwError=true'
```
- Retries: 3 attempts
- Delay: 1 second between retries

#### Bulkhead Example
```bash
curl 'http://localhost:8080/bulkhead?name=user1'
```
- Max concurrent calls: 3
- Max wait duration: 500ms

#### Circuit Breaker Example
```bash
curl 'http://localhost:8080/circuit'
```
- Failure threshold: 50%
- Sliding window size: 5
- Wait duration in open state: 10s

### Monitoring
#### Actuator Endpoints
The application exposes various actuator endpoints for monitoring:

- Health: http://localhost:8080/actuator/health
- Metrics: http://localhost:8080/actuator/metrics
- Circuit Breaker Status: http://localhost:8080/actuator/circuitbreakers
- Rate Limiters: http://localhost:8080/actuator/ratelimiters

### Dependencies
- Spring Boot
- Resilience4j
- Spring Actuator
- Lombok
- Caffeine Cache


