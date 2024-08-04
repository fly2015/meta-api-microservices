### What is short cuircut in the distributed system?
In the context of distributed systems, a circuit breaker is a design pattern that allows a system to detect failures and encapsulates the logic of preventing a failure from constantly recurring, during maintenance, temporary external system failure or unexpected system difficulties.

The circuit breaker pattern is named after the electrical circuit breaker that makes a circuit fail fast when an anomaly is detected.

In a distributed environment, inter-service calls may fail due to unavailability of the called service, network issues, etc. Continuously making a call to a service that is unavailable or unable to handle the request will result in resource exhaustion.

The circuit breaker pattern can prevent an application from repeatedly trying to execute an operation that's likely to fail. It allows services to fail fast and recover as soon as possible, improving the overall resilience of the system.

Here's a simplified explanation of how it works:

1. **Closed State**: The circuit breaker allows traffic to pass through until the number of failures hits a specified threshold. After the threshold is reached, it opens the circuit.

2. **Open State**: The circuit breaker returns an error for calls without executing the function, and after a certain amount of time, it enters a half-open state.

3. **Half-Open State**: In this state, the circuit breaker allows a limited number of calls to pass through. If those requests succeed, the circuit breaker resumes normal operation (back to Closed State). If they fail, it re-opens the circuit (back to Open State).

This pattern is often used in microservices architecture where it's crucial to prevent cascading failures across multiple services.

### What are some common use cases for the circuit breaker pattern in distributed systems?
The Circuit Breaker pattern is commonly used in distributed systems to prevent a failure from constantly recurring, during maintenance, temporary external system failure, or unexpected system difficulties. Here are some common use cases:

1. **Microservices Communication**: In a microservices architecture, services often need to communicate with each other. If one service is down or experiencing high latency, the circuit breaker can prevent further requests to the failing service, thus preventing the failure from cascading to other services.

2. **Database Access**: If a system is experiencing issues connecting to a database due to network issues or the database being down, a circuit breaker can be used to quickly fail requests to the database instead of waiting for a timeout.

3. **External API Calls**: When a system makes calls to external APIs, there's always a risk that the external service might be down or slow to respond. A circuit breaker can be used to prevent the system from being bogged down by slow or failing external API calls.

4. **Rate Limiting**: If a service is being overwhelmed with requests, a circuit breaker can be used to limit the rate of incoming requests to prevent the service from crashing.

5. **Infrastructure Failures**: In cloud environments, infrastructure components like storage, networking, or compute resources can fail. Circuit breakers can help applications fail fast and recover from these failures.

6. **Third-Party Service Providers**: If your application relies on third-party services for things like payment processing, email services, or cloud storage, a circuit breaker can prevent your application from becoming unresponsive if the third-party service is slow or unavailable.

### How we trafic request to other service when circuit opened?
When the circuit is open in a Circuit Breaker pattern, it means that the system has detected that the downstream service is unavailable or is not functioning properly. In this state, the system will not send any requests to the downstream service to prevent further failures and to allow the downstream service to recover.

However, if you still want to handle the requests when the circuit is open, you can use a fallback mechanism. A fallback is a predefined procedure that your application can follow when a call to a service fails. This could be:

1. Returning a default value.
2. Serving data from a cache.
3. Calling another service.
4. Queuing the request for retry later.

Here's a simple example in Java using the Hystrix library, which supports the Circuit Breaker pattern and fallback mechanisms:

```java
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class FallbackCommand extends HystrixCommand<String> {

    public FallbackCommand() {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
    }

    @Override
    protected String run() throws Exception {
        throw new RuntimeException("This command always fails.");
    }

    @Override
    protected String getFallback() {
        return "Hello from fallback!";
    }
}
```

In this example, the `run` method simulates a command that always fails. When this happens, Hystrix opens the circuit and calls the `getFallback` method to handle the request. The fallback method returns a default value.

### how to handle the circuit breaker?
Handling the circuit breaker pattern in a distributed system involves three main steps: detecting failures, opening the circuit, and attempting recovery. Here's a basic example of how you might implement this in a Spring Boot application using the Resilience4j library:

1. Add the Resilience4j dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-spring-boot2</artifactId>
    <version>1.7.0</version>
</dependency>
```

2. Configure the circuit breaker in your `application.yml`:

```yaml
resilience4j:
  circuitbreaker:
    instances:
      myService:
        registerHealthIndicator: true
        slidingWindowSize: 10
        minimumNumberOfCalls: 5
        permittedNumberOfCallsInHalfOpenState: 3
        automaticTransitionFromOpenToHalfOpenEnabled: true
        waitDurationInOpenState: 10s
        failureRateThreshold: 50
        eventConsumerBufferSize: 10
```

3. Use the circuit breaker in your service:

```java
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

@Service
public class MyService {

    @CircuitBreaker(name = "myService", fallbackMethod = "fallback")
    public String processOrder(Order order) {
        // Your logic to process the order
    }

    public String fallback(Order order, Throwable t) {
        // Your fallback logic
    }
}
```

In this example, the `@CircuitBreaker` annotation tells Resilience4j to wrap the `processOrder` method with a circuit breaker. If this method throws an exception, Resilience4j will increment the failure count in the circuit breaker. If the failure rate goes above the threshold defined in the configuration (`failureRateThreshold: 50`), the circuit breaker will open and start to reject calls. When a call is rejected, the `fallback` method is called. After a certain amount of time (`waitDurationInOpenState: 10s`), the circuit breaker will allow a few calls to go through to see if the backend service is still failing or has become available again.

### What is Hystrix
Hystrix is a latency and fault tolerance library designed to isolate points of access to remote systems, services and 3rd party libraries, stop cascading failure and enable resilience in complex distributed systems where failure is inevitable.

Hystrix does this by implementing what's known as the circuit breaker pattern. When calls to a particular service exceed a certain threshold, Hystrix trips the circuit breaker and all further calls to that service will fail immediately without any network request. After a recovery period, Hystrix will allow a limited amount of requests to pass through and if those requests succeed, the circuit breaker will be closed again, otherwise it remains open.

Hystrix also provides a fallback mechanism where a default value or action can be defined in case of failure, improving the overall resilience of the system.

Hystrix was originally developed by Netflix to control the interactions between their many services to provide greater latency and fault tolerance. It's part of the larger Spring Cloud family of projects for building cloud-based applications, but as of March 2019, the project is in maintenance mode, with no further development planned.

### What is Resilience4j
Resilience4j is a lightweight fault tolerance library inspired by Netflix Hystrix, but designed for functional programming in Java 8 and higher. It provides higher-order functions (decorators) to enhance any functional interface, lambda expression, or method reference with a Circuit Breaker, Rate Limiter, Retry, Bulkhead, or Time Limiter.

### what are the others pattern or technics that similar circuit breaker or support circuit breaker that help prevent service cascade?
There are several design patterns and techniques that can be used in conjunction with the Circuit Breaker pattern to prevent cascading failures in a distributed system:

1. **Retry Pattern**: This pattern defines a strategy for retrying an operation in case of a failure. The strategy can define the maximum number of retry attempts, delays between attempts, and conditions on which to abort the retry attempts.

2. **Fallback Pattern**: This pattern provides an alternative solution or default behavior when a service call or operation fails. It's often used in conjunction with the Circuit Breaker pattern to provide a response even when the circuit is open.

3. **Bulkhead Pattern**: This pattern isolates elements of an application into pools so that if one fails, the others will continue to function. It's named after the sectioned partitions (bulkheads) of a ship's hull. If the hull of a ship is compromised, only the damaged section fills with water, which prevents the ship from sinking.

4. **Throttling Pattern**: This pattern limits the number of requests or the total amount of data that a client can request from a service over a period of time. This can help to prevent a service from being overwhelmed by too many requests.

5. **Timeout Pattern**: This pattern sets a limit on the amount of time a request should be allowed to execute. If the request doesn't complete within the specified time limit, it's automatically cancelled.

6. **Load Balancing Pattern**: This pattern distributes requests or tasks across multiple resources to avoid overloading a single resource and to improve overall system performance and availability.

7. **Health Check Pattern**: This pattern periodically checks the health of services and resources to catch potential issues early and prevent failures. If a service is detected as unhealthy, it can be taken out of rotation to prevent it from receiving more requests.

These patterns can be used individually or in combination to build resilient and fault-tolerant distributed systems.

### What is the difference between circuit breaker and retry pattern?

### So today we should use Resilence4j instead of hyxtrix?
Yes, it is generally recommended to use Resilience4j instead of Hystrix for new projects. Hystrix is currently in maintenance mode and is no longer actively developed, as announced by Netflix in 2018.

On the other hand, Resilience4j is actively maintained and offers similar features to Hystrix such as Circuit Breaker, Rate Limiter, Retry, and Bulkhead along with some additional features. It is also lightweight and designed for Java 8 and functional programming, which makes it a good fit for modern applications.

However, if you have an existing application that already uses Hystrix and it meets your needs, there may not be a compelling reason to switch. It's always important to evaluate these decisions in the context of your specific use case and requirements.

### What is the difference between circuit breaker and rate limiting?
The Circuit Breaker and Rate Limiting are both design patterns used in distributed systems to prevent failures and maintain system stability. However, they serve different purposes and are used in different scenarios:

**Circuit Breaker Pattern**: This pattern is used to detect failures and encapsulate the logic of preventing a failure from constantly recurring. It's like an automatic switch that stops the flow of calls to a service that is failing or is down. The main purpose of the circuit breaker is to give the failing service time to recover and prevent the failure from cascading to other parts of the system. Once the service is up again, the circuit breaker allows the flow of calls to resume.

**Rate Limiting Pattern**: This pattern is used to control the number of requests a client can make to a service in a specific period of time. The main purpose of rate limiting is to protect a service from being overwhelmed by too many requests at once. This can be particularly useful in scenarios where you want to prevent abuse, manage resource utilization, or maintain quality of service for all users.

In summary, while both patterns help to maintain system stability, the circuit breaker does so by preventing overloading a failing service, while rate limiting does so by controlling the overall number of requests a service has to handle at a time.

### What are some best practices for implementing the Circuit Breaker pattern?
Implementing the Circuit Breaker pattern effectively requires careful consideration of several factors. Here are some best practices:

1. **Define a suitable failure threshold**: The failure threshold should be set according to the nature of your application and the service you are calling. It should be high enough to avoid unnecessary circuit trips due to minor issues, but low enough to protect your system from a failing service.

2. **Choose an appropriate timeout duration**: The timeout duration should be long enough to give the failing service a chance to recover, but not so long that it causes unnecessary delays in your application.

3. **Implement a fallback mechanism**: A fallback mechanism is crucial to provide a default behavior when the circuit is open. This could be serving stale data, default values, or even redirecting to another service.

4. **Monitor and log circuit breaker events**: Monitoring the state of your circuit breakers can provide valuable insights into the health of your services. Logging circuit breaker events can help in debugging and identifying issues.

5. **Test your circuit breaker implementation**: It's important to test your circuit breaker under different scenarios to ensure it behaves as expected. This includes testing under normal conditions, when the service is failing, and when the service recovers.

6. **Gradually roll out changes**: When introducing or modifying a circuit breaker, it's a good practice to gradually roll out changes and closely monitor the impact. This can help to catch any potential issues early.

7. **Use existing libraries**: There are several libraries available that provide implementations of the Circuit Breaker pattern, such as Netflix's Hystrix and Resilience4J. Using these libraries can save development time and help to avoid potential pitfalls.

8. **Consider the impact on the system as a whole**: The Circuit Breaker pattern can help to protect individual services, but it's also important to consider the impact on the system as a whole. For example, if a circuit breaker trips frequently, it could be a sign of a larger issue that needs to be addressed.

### Give me an example of "Bulkhead Pattern"?
The Bulkhead pattern is a type of application design pattern that is used to prevent failures in one part of the system from cascading to other parts. It's named after the sectioned partitions (bulkheads) of a ship's hull. If the hull of a ship is compromised, only the damaged section fills with water, which prevents the ship from sinking.

In the context of microservices, you can think of each service as a compartment within a ship. If one service fails, it doesn't affect the others, thus preventing a single point of failure from bringing down the entire system.

Here's a simple example of how you might implement the Bulkhead pattern in Java using the Resilience4j library:

First, add the Resilience4j dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-bulkhead</artifactId>
    <version>1.7.0</version>
</dependency>
```

Then, you can create a `Bulkhead` and use it to decorate a method call:

```java
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;

import java.time.Duration;

public class BulkheadExample {

    public static void main(String[] args) {
        // Create a custom configuration for a Bulkhead
        BulkheadConfig config = BulkheadConfig.custom()
            .maxConcurrentCalls(10)
            .maxWaitDuration(Duration.ofMillis(500))
            .build();

        // Create a BulkheadRegistry with the custom configuration
        BulkheadRegistry bulkheadRegistry = BulkheadRegistry.of(config);

        // Use the registry to create bulkheads
        Bulkhead bulkhead = bulkheadRegistry.bulkhead("myService");

        // Decorate your call to backendService.doSomething():
        CheckedFunction0<String> decoratedSupplier = Bulkhead
            .decorateCheckedSupplier(bulkhead, backendService::doSomething);

        Try<String> result = Try.of(decoratedSupplier)
            .recover(throwable -> "Hello from Bulkhead!");

        System.out.println(result.isSuccess());
    }
}
```

In this example, the `Bulkhead` is configured to allow a maximum of 10 concurrent calls. If all 10 are busy when a new call comes in, the new call will wait for up to 500 milliseconds for a spot to free up. If a spot doesn't free up in that time, the call will fail with a `BulkheadFullException`. The `recover` method provides a fallback response in this case.
