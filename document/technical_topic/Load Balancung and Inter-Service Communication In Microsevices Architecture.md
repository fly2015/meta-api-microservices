### Can discovery service handle the availability
Yes, the Discovery Service in a microservices architecture, such as Netflix's Eureka, can handle the availability of services. It allows microservices to register themselves and discover other services, enabling them to handle requests appropriately even if the location of other services changes.

In your `application.properties` file, you have enabled Eureka's client-side discovery by setting `eureka.client.fetch-registry` to `true`. This means your service will fetch the registry from the Eureka server and cache it locally. This allows your service to make requests to other services using their application names instead of hard-coded URLs.

If a service goes down, Eureka will not immediately remove it from the registry. Instead, it will mark the service as unavailable and try to reconnect. If the service does not become available after a certain period, Eureka will then remove it from the registry. This helps to handle temporary network issues or service restarts without affecting the overall system.

In addition, you have enabled the use of Eureka with Spring Cloud Gateway by setting `spring.cloud.gateway.discovery.locator.enabled` to `true`. This means that Spring Cloud Gateway will use the service registry to route requests to the appropriate services.

So, in your current setup, the Discovery Service (Eureka) is configured to handle the availability of your microservices.

### How to register more than one service to load balance and increase availability
To register more than one instance of a service for load balancing and increased availability, you can run multiple instances of your service on different ports. Each instance will register itself with Eureka, and Eureka will then balance the load among them.

Here's how you can do it:

1. First, make sure that your service is configured to register with Eureka. This is done by setting `eureka.client.register-with-eureka` to `true` in your `application.properties` file. From the code you provided, it seems like this is already the case.

2. Next, you need to run multiple instances of your service. You can do this by running your application multiple times, each time with a different server port. You can specify the server port by setting the `server.port` property in your `application.properties` file.

For example, if you want to run two instances of your `book-service`, you can create two `application.properties` files: one for each instance. The first file could look like this:

```ini
server.port=50001
```

And the second file could look like this:

```ini
server.port=50002
```

Then, you can run each instance with its own `application.properties` file. In IntelliJ IDEA, you can do this by creating two Run Configurations, each with a different `spring.config.name` parameter pointing to the corresponding `application.properties` file.

3. Once your services are running and registered with Eureka, you can use Spring Cloud's `@LoadBalanced` annotation to balance the load among the instances. This can be done in a `RestTemplate` bean definition like this:

```java
@Bean
@LoadBalanced
public RestTemplate restTemplate() {
    return new RestTemplate();
}
```

With this setup, when you make a request to your service using the `RestTemplate`, Spring Cloud will automatically load balance the request among the available instances.

Remember, running multiple instances of a service increases resource usage, so make sure your system has enough resources to handle the increased load.
### How Discovery service work?
The Discovery Service in a microservices architecture is a key component that allows microservices to register themselves and discover other services. This enables them to handle requests appropriately even if the location of other services changes.

Here's a step-by-step explanation of how a Discovery Service, such as Netflix's Eureka, works:

1. **Registration**: When a microservice starts up, it registers itself with the Discovery Service. This registration includes metadata such as the host and port, the service ID, and health indicator URLs.

2. **Discovery**: Other microservices can query the Discovery Service to find out how to reach a service. This is typically done using the service ID. The Discovery Service returns the host and port for the service instance.

3. **Health Check**: The Discovery Service regularly checks the health of the services. If a service instance becomes unresponsive, the Discovery Service will remove it from the registry.

4. **Load Balancing**: The Discovery Service can also provide load balancing. When a client requests a service, the Discovery Service can return the instance with the least load.

In your `application.properties` file, you have enabled Eureka's client-side discovery by setting `eureka.client.fetch-registry` to `true`. This means your service will fetch the registry from the Eureka server and cache it locally. This allows your service to make requests to other services using their application names instead of hard-coded URLs.

If a service goes down, Eureka will not immediately remove it from the registry. Instead, it will mark the service as unavailable and try to reconnect. If the service does not become available after a certain period, Eureka will then remove it from the registry. This helps to handle temporary network issues or service restarts without affecting the overall system.

In addition, you have enabled the use of Eureka with Spring Cloud Gateway by setting `spring.cloud.gateway.discovery.locator.enabled` to `true`. This means that Spring Cloud Gateway will use the service registry to route requests to the appropriate services.

### What is default zone in Eureka?
The `defaultZone` in Eureka is the URL where the Eureka server is running. It's the location where the Eureka client will register itself and fetch the registry of other services.

In your case, the `eureka.client.serviceUrl.defaultZone` property is set to `http://localhost:50005/eureka/`. This means your Eureka server is running on the local machine (`localhost`) on port `50005`.

When a service starts up, it will register itself with the Eureka server at this URL. It will also fetch the registry from this URL to discover other services. This is controlled by the `eureka.client.fetch-registry` and `eureka.client.register-with-eureka` properties, which are both set to `true` in your `application.properties` file.

### How to custom the defaultZone in Eureka?
To customize the `defaultZone` in Eureka, you need to modify the `eureka.client.serviceUrl.defaultZone` property in your `application.properties` file. This property should be set to the URL where your Eureka server is running.

For example, if your Eureka server is running on a different machine with the IP address `192.168.1.100` and port `8761`, you can set the `defaultZone` like this:

```ini
eureka.client.serviceUrl.defaultZone=http://192.168.1.100:8761/eureka/
```

Remember to replace `192.168.1.100:8761` with the actual IP address and port where your Eureka server is running.

After making this change, your service will register itself with the Eureka server at the new URL and fetch the registry from this URL to discover other services.
### What is eureka replicas?
Eureka replicas refer to multiple instances of the Eureka server running in a cluster for high availability and fault tolerance. By running multiple replicas of the Eureka server, you can ensure that the service registry remains available even if one of the instances goes down.
Eureka Replicas refer to the instances of Eureka Servers that are part of a Eureka Cluster. In a production environment, it's recommended to run Eureka Server in a peer-aware mode which allows for multiple Eureka Servers to be aware of each other. This is done for redundancy and to ensure high availability of the service registry.

When a Eureka Client registers with a Eureka Server, that server replicates the registration to all other peer servers. This way, all Eureka Servers maintain the same state of the service registry. If one server goes down, the others can still provide the necessary information to the clients.

In your `application.properties` file, the `eureka.client.serviceUrl.defaultZone` property is used to specify the list of Eureka Servers that the client will connect to. If you have multiple Eureka Servers, you can specify them as a comma-separated list like this:

```ini
eureka.client.serviceUrl.defaultZone=http://eureka1:8761/eureka/,http://eureka2:8762/eureka/
```

In this example, `eureka1` and `eureka2` are the hostnames of the Eureka Servers, and `8761` and `8762` are their respective ports. The Eureka Client will connect to these servers and treat them as replicas of each other.

### How we do the replicas for api-gateway?
To set up replicas for the API Gateway service, you can run multiple instances of the `api-gateway` service on different ports. Each instance will register itself with Eureka, and Eureka will then balance the load among them.
To create replicas for the `api-gateway`, you would follow a similar process to creating replicas for any other service. You would need to run multiple instances of the `api-gateway` service, each on a different port.

Here's how you can do it:

1. First, you need to create multiple `application.properties` files for each instance of the `api-gateway` service. Each file should specify a different server port.

For example, you could create two `application.properties` files: one for each instance. The first file could look like this:

```ini
server.port=50004
```

And the second file could look like this:

```ini
server.port=50006
```

2. Then, you can run each instance with its own `application.properties` file. In IntelliJ IDEA, you can do this by creating two Run Configurations, each with a different `spring.config.name` parameter pointing to the corresponding `application.properties` file.

3. Once your `api-gateway` services are running and registered with Eureka, they will be available for load balancing. When a request comes in, Eureka will balance the load among the available `api-gateway` instances.

Remember, running multiple instances of a service increases resource usage, so make sure your system has enough resources to handle the increased load.

### Where is the single point that accept request from client?
In your microservices architecture, the single point that accepts requests from clients is the API Gateway. In your case, it's the `api-gateway` service.

The API Gateway is responsible for request routing, composition, and protocol translation. It provides each of the application's clients with a custom API. The API Gateway handles incoming requests and forwards them to the appropriate microservice.

In your `api-gateway` service, you have enabled the use of Eureka with Spring Cloud Gateway by setting `spring.cloud.gateway.discovery.locator.enabled` to `true` in the `application.properties` file. This means that Spring Cloud Gateway will use the service registry from Eureka to route requests to the appropriate services.

The `api-gateway` service is running on port `50004` as specified by the `server.port` property in the `application.properties` file. Therefore, clients should send their requests to this port.

### It's mean I should have only one api gateway?
Yes, typically in a microservices architecture, there is only one API Gateway. The API Gateway acts as a single entry point for all client requests. It routes incoming requests to the appropriate microservice based on the request path, method, and other parameters.

However, it's important to note that while there is usually only one logical API Gateway, it can be deployed as multiple instances for redundancy and to handle high loads. This is where Eureka and load balancing come into play. Eureka will distribute the load among the available instances of the API Gateway.

In your case, the `api-gateway` service is your API Gateway. If you want to handle more traffic, you can run multiple instances of the `api-gateway` service, each on a different port. These instances will register themselves with Eureka and Eureka will balance the load among them.

### For example my api-gateway is localhost:50004, localhost:50008, how client access them with load balancing?
In a microservices architecture, client-side load balancing can be achieved using Netflix Ribbon which is integrated with Spring Cloud. Ribbon is a client-side load balancer that gives you a lot of control over the behavior of HTTP and TCP clients.

When a client makes a request through the API Gateway, Ribbon will balance the load among the available instances of the API Gateway. Ribbon uses the service registry information from Eureka to locate available instances and their health status.

However, the client doesn't directly interact with Ribbon or Eureka. Instead, the client sends a request to a single, well-known address. This could be a DNS name that is mapped to multiple IP addresses, or a load balancer that forwards the request to one of the available instances.

In your case, if you have multiple instances of the `api-gateway` service running on `localhost:50004` and `localhost:50008`, you would need a load balancer in front of these instances. This load balancer would accept incoming requests and forward them to one of the available `api-gateway` instances.

There are many load balancers available, such as Nginx, HAProxy, or cloud-based solutions like AWS Elastic Load Balancer or Google Cloud Load Balancer. The choice of load balancer would depend on your specific requirements and infrastructure.

Here's an example of how you could configure Nginx to load balance between your two `api-gateway` instances:

```nginx
http {
    upstream api_gateway {
        server localhost:50004;
        server localhost:50008;
    }

    server {
        listen 80;

        location / {
            proxy_pass http://api_gateway;
        }
    }
}
```

In this example, Nginx listens on port 80 and forwards incoming requests to either `localhost:50004` or `localhost:50008`. The client would then send their requests to `localhost:80`.

Remember to replace `localhost:80` with the actual IP address and port where your load balancer is running.


### Give me example with Ribbon?
Sure, Ribbon is a client-side load balancer that works well with Spring Cloud and Eureka. Here's an example of how you might use Ribbon in a Spring Boot application.

First, you need to include the necessary dependencies in your `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

Then, in your application, you can use the `@LoadBalanced` annotation to indicate that you want to use Ribbon for load balancing:

```java
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RibbonConfiguration {

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

In this example, the `RestTemplate` bean is annotated with `@LoadBalanced`, which means it will use Ribbon for load balancing.

Then, when you want to make a request to a service, you can use the service's name instead of its URL:

```java
@Autowired
private RestTemplate restTemplate;

public String callService(String serviceName) {
    String response = restTemplate.getForObject("http://" + serviceName + "/endpoint", String.class);
    return response;
}
```

In this example, `serviceName` is the name of the service as registered with Eureka. Ribbon will use Eureka to discover available instances of the service and load balance the request across them.

Please note that as of Spring Cloud 2020.0.0 (Ilford) and later, the Netflix Ribbon and Netflix Hystrix integrations are in maintenance mode. The Spring Cloud LoadBalancer and Spring Cloud CircuitBreaker projects should be used instead.

### With Ribbon, what is the url client will request to use one of between localhost:50004 or localhost:50008
With Ribbon, the client doesn't need to know the specific URLs of the `api-gateway` instances. Instead, the client will make a request to the service name as registered with Eureka. Ribbon will use Eureka to discover available instances of the service and load balance the request across them.

In your case, the service name is `api-gateway` as specified by the `spring.application.name` property in the `application.properties` file. So, the client would make a request to `http://api-gateway/endpoint`.

Here's an example of how you might make a request in a Spring Boot application:

```java
@Autowired
private RestTemplate restTemplate;

public String callApiGateway() {
    String response = restTemplate.getForObject("http://api-gateway/endpoint", String.class);
    return response;
}
```

In this example, `http://api-gateway/endpoint` is the URL that the client will request. Ribbon will intercept this request and replace `api-gateway` with one of the available instances' URLs (`localhost:50004` or `localhost:50008`), effectively load balancing the request.

Please note that the client and the `api-gateway` instances need to be part of the same Eureka service registry for this to work.

### How server can reconize the http://api-gateway/endpoint from client request?
The server recognizes the `http://api-gateway/endpoint` from the client request through the service discovery mechanism provided by Eureka and the routing capabilities of Spring Cloud Gateway.

When the `api-gateway` service starts, it registers itself with Eureka, the service registry. It provides its service name (`api-gateway`), host, and port. Eureka then knows that the `api-gateway` service is available for use.

When a client makes a request to `http://api-gateway/endpoint`, the following happens:

1. The client's request is intercepted by Spring Cloud Gateway.
2. Spring Cloud Gateway sees that the request is intended for the `api-gateway` service.
3. It then consults Eureka to find out where the `api-gateway` service is running.
4. Eureka responds with the host and port where the `api-gateway` service is available.
5. Spring Cloud Gateway then forwards the client's request to the appropriate instance of the `api-gateway` service.

This process is transparent to the client. The client only needs to know the service name (`api-gateway`) and the endpoint it wants to access (`/endpoint`). The actual routing is handled by Spring Cloud Gateway and Eureka.

This setup allows for flexibility and scalability. If the `api-gateway` service is scaled up to run on multiple instances, Eureka will know about all of them. When a request comes in for the `api-gateway` service, Spring Cloud Gateway can use this information to load balance the request across the available instances.

### Do I need a load balancer in front of these instances to accept the url http://api-gateway/endpoint from the internet?
Yes, you would typically need a load balancer in front of your instances to accept requests from the internet. The load balancer would be responsible for distributing incoming network traffic across multiple servers to ensure that no single server becomes overwhelmed with too much traffic. This helps to increase the availability and reliability of your applications.

In the context of your `api-gateway` running on `localhost:50004` and `localhost:50008`, a load balancer would accept incoming requests and forward them to one of these instances. The client would send their requests to the load balancer, and the load balancer would route the request to the appropriate instance.

Here's an example of how you could configure Nginx, a popular open-source load balancer, to distribute traffic between your two `api-gateway` instances:

```nginx
http {
    upstream api_gateway {
        server localhost:50004;
        server localhost:50008;
    }

    server {
        listen 80;

        location / {
            proxy_pass http://api_gateway;
        }
    }
}
```

In this configuration, Nginx listens on port 80 and forwards incoming requests to either `localhost:50004` or `localhost:50008`. The client would then send their requests to `localhost:80`.

Remember to replace `localhost:80` with the actual IP address and port where your load balancer is running if you're deploying this in a production environment.

### Then, what is the place for Ribbon?
Ribbon is a client-side load balancer that provides control over HTTP and TCP clients. In a microservices architecture, it is used to route requests to different instances of a service, providing load balancing at the client level.

In your setup, Ribbon would be used in the services that are making requests to the `api-gateway`. These services would use Ribbon to determine which instance of the `api-gateway` to send their requests to. Ribbon uses the information from the Eureka service registry to make this decision.

Here's a simplified view of how it works:

1. A service (let's call it `service-A`) needs to make a request to the `api-gateway`.
2. `service-A` asks Ribbon for the address of the `api-gateway`.
3. Ribbon consults Eureka to get the list of available instances of the `api-gateway`.
4. Ribbon applies its load balancing algorithm to select one of the available instances.
5. Ribbon returns the address of the selected instance to `service-A`.
6. `service-A` makes the request to the selected instance of the `api-gateway`.

This process is transparent to `service-A`. From its perspective, it's simply making a request to the `api-gateway`. The load balancing is handled by Ribbon.

Please note that as of Spring Cloud 2020.0.0 (Ilford) and later, the Netflix Ribbon and Netflix Hystrix integrations are in maintenance mode. The Spring Cloud LoadBalancer and Spring Cloud CircuitBreaker projects should be used instead.

### But the server loadbalancer already routed requests to different gateway api
Yes, you're correct. If you have a server-side load balancer (like Nginx) in front of your `api-gateway` instances, it can distribute incoming requests across these instances. This is a common setup for handling incoming traffic from the internet.

However, Ribbon, as a client-side load balancer, operates at a different level. It's used within your microservices architecture, by the services that are making requests to the `api-gateway`. These services use Ribbon to determine which instance of the `api-gateway` to send their requests to, based on the information from the Eureka service registry.

So, in a typical setup:

1. External requests from the internet are first handled by a server-side load balancer (like Nginx), which routes the requests to one of the `api-gateway` instances.
2. Within your microservices architecture, services making requests to the `api-gateway` use Ribbon for client-side load balancing.

This way, you have load balancing at both the entry point of your architecture (server-side load balancing) and at the level of inter-service communication (client-side load balancing). This setup can provide a robust and flexible system for handling and routing requests.

### Why internal service need to call api-gateway? It should be not cross call.
In a microservices architecture, services often need to communicate with each other to perform their tasks. This inter-service communication can be direct (one service calling another service directly) or indirect (one service calling another service through an API gateway).

The use of an API gateway for inter-service communication has several advantages:

1. **Centralized Access Control**: The API gateway can handle authentication and authorization, ensuring that only authorized services can access certain endpoints. This reduces the need for each service to implement its own access control logic.

2. **Rate Limiting and Throttling**: The API gateway can control the rate of requests to prevent any service from being overwhelmed with too many requests.

3. **Monitoring and Logging**: The API gateway can log all incoming and outgoing requests, providing a centralized place for monitoring and debugging.

4. **Request Routing and Load Balancing**: The API gateway can route requests to different instances of a service based on various factors like load, latency, etc.

However, there are also some potential downsides to using an API gateway for inter-service communication:

1. **Increased Latency**: Since requests have to go through the API gateway, this can add some additional latency.

2. **Single Point of Failure**: If the API gateway goes down, it can affect all services that rely on it for communication.

3. **Complexity**: The API gateway adds another layer of complexity to the system.

In your case, if the services in your architecture do not need the features provided by the API gateway, or if the downsides outweigh the benefits, it might make sense to have services communicate directly with each other instead of going through the API gateway. This decision depends on the specific requirements and constraints of your system.

### Give me an example for inter-service communication?
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
public class BookService {

    private final RestTemplate restTemplate;

    @Autowired
    public BookService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Review> getReviewsForBook(Long bookId) {
        String reviewServiceUrl = "http://review-service/reviews/book/" + bookId;
        Review[] reviews = restTemplate.getForObject(reviewServiceUrl, Review[].class);
        return Arrays.asList(reviews);
    }
}

### To achieve load balancer, we should inter call via api-gateway?
Yes, using an API Gateway can help achieve load balancing in a microservices architecture. The API Gateway can distribute incoming requests across multiple instances of a service, effectively balancing the load.

When a service needs to communicate with another service, it can send the request to the API Gateway. The API Gateway, knowing all the available instances of the target service (through service discovery), can then route the request to one of these instances based on its load balancing policy.

This setup allows for load balancing at the level of inter-service communication. It's important to note that this is in addition to any server-side load balancing you might have for handling incoming traffic from the internet.

However, whether or not to use an API Gateway for inter-service communication depends on your specific requirements and constraints. If the services in your architecture do not need the features provided by the API Gateway, or if the downsides (like increased latency, single point of failure, and added complexity) outweigh the benefits, it might make sense to have services communicate directly with each other.

### Why Eureka do not load balance by its self, why it need to have ribbon?
Eureka and Ribbon serve different purposes in a microservices architecture, and they work together to provide load balancing functionality.

Eureka is a service registry. Its primary function is to allow services to register themselves and to allow other services to discover them. When a service starts up, it registers with Eureka, providing its host and port. Other services can then query Eureka to find out where a particular service is running.

Ribbon, on the other hand, is a client-side load balancer. It uses the information from Eureka to list all available instances of a service and applies load balancing algorithms to decide which instance to route a request to.

The reason why Eureka doesn't handle load balancing itself is because of the separation of concerns principle. Eureka's job is to manage service registration and discovery, not to decide how requests should be distributed among service instances. That's the job of a load balancer, and in this case, it's handled by Ribbon on the client side.

This separation allows each component to do its job well and provides flexibility. For example, you could replace Ribbon with another load balancing solution if you wanted to, and Eureka would still continue to function as your service registry.

### What are some common load balancing algorithms used in microservices architecture?
In a microservices architecture, load balancing is crucial for distributing network traffic across multiple servers to ensure no single server becomes overwhelmed. Here are some common load balancing algorithms used:

1. **Round Robin**: This is the simplest method of load balancing. Requests are distributed across the servers in rotation. If there are three servers, the first request goes to the first server, the second request goes to the second server, the third request goes to the third server, the fourth request goes back to the first server, and so on.

2. **Least Connections**: In this method, the load balancer keeps track of the number of active connections to each server and sends new requests to the server with the fewest active connections. This is useful when requests take varying amounts of processing time.

3. **Weighted Round Robin or Weighted Least Connections**: These are variations of the Round Robin and Least Connections algorithms. Each server is assigned a weight that indicates its processing capacity. Servers with higher weights receive a proportionally larger number of requests.

4. **Random**: The load balancer selects a random server for each request. This can be a good choice when all servers are similar in terms of their processing power and the time taken to process requests.

5. **IP Hash**: The IP address of the client is used to determine which server receives the request. This can ensure that a client is always served by the same server, which can be useful for maintaining session data.

6. **Least Response Time**: This method directs traffic to the server with the fewest active connections and the lowest average response time.

7. **Consistent Hashing**: This is a more advanced load balancing method that is particularly useful in distributed systems. It minimizes reorganization of the system when nodes are added or removed, thus reducing the impact on cache memory.

Remember, the choice of load balancing algorithm depends on the specific requirements and constraints of your system.

### What is replacement for eureka and Spring gateway?
There are several alternatives to Eureka and Spring Cloud Gateway in the microservices ecosystem:

1. **Service Discovery (Alternative to Eureka)**:
    - **Consul**: Developed by HashiCorp, Consul is a solution that provides service discovery, configuration, and orchestration capabilities. It is widely used due to its multi-datacenter support and has a DNS interface that can be used for service discovery.
    - **Zookeeper**: Apache Zookeeper is a centralized service for maintaining configuration information, naming, providing distributed synchronization, and providing group services. It is robust and mature, and used by many big data technologies as their underlying coordination system.
    - **etcd**: Developed by CoreOS, etcd is a strongly consistent, distributed key-value store that provides a reliable way to store data that needs to be accessed by a distributed system or cluster of machines.

2. **API Gateway (Alternative to Spring Cloud Gateway)**:
    - **Kong**: Kong is a scalable, open source API Gateway that runs in front of any RESTful API and is extended through plugins, which provide extra functionalities and services beyond the core platform.
    - **Zuul**: Zuul is a gateway service that provides dynamic routing, monitoring, resiliency, security, and more. Zuul was also developed by Netflix and is often used in Spring Cloud applications, although it's worth noting that as of Spring Cloud Netflix 2.2.0, Zuul is in maintenance mode and the Spring Cloud team suggests Spring Cloud Gateway for new projects.
    - **Traefik**: Traefik is a modern HTTP reverse proxy and load balancer that makes deploying microservices easy. It supports several backends (Docker, Swarm, Kubernetes, Marathon, Consul, Etcd, Zookeeper, BoltDB, Rest API, file...) to manage its configuration automatically and dynamically.

Remember, the choice of service discovery and API gateway solutions depends on your specific requirements and constraints.