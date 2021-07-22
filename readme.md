# STORE APPLICATION

Store app is a parent project which contains;
* An api-gateway that is developed using [Spring Cloud Gateway](https://github.com/spring-cloud/spring-cloud-gateway).
  * store-gateway-service
* Two microservices that are developed using [Spring Boot](https://github.com/spring-projects/spring-boot).
  * store-product-service
  * store-order-service

## store-gateway-service

### Running the app at local environment
store-gateway-service is a Spring Cloud Gateway app built with maven.  
The gateway can be built to produce a jar file. Jar file can be run as given below;

```
git clone https://github.com/ozdemirkan/store.git
cd store/
./mvnw package
java -jar store-gateway-service/target/store-gateway-service.jar
```

App can be accessed at: http://localhost:8080/

store-gateway-service accepts REST API calls from clients and routes them to related microservice.
It uses configured route definitions to uris to determine the correct microservice for each API call.
```
### application.yml ###

spring:
  cloud:
    gateway:
      routes:
        - id: product
          uri: http://localhost:8081
          predicates:
            - Path=/api/product/**
        - id: order
          uri: http://localhost:8082
          predicates:
            - Path=/api/order/**

```


## store-product-service 

### Running the app at local environment
store-product-service is a Spring Boot app built with maven.
The app can be built to produce a jar file. Jar file can be run as given below;

```
git clone https://github.com/ozdemirkan/store.git
cd store/
./mvnw package
java -jar store-product-service/target/store-product-service.jar
```

App can be accessed at: http://localhost:8081/
Api documentation can be accessed at: http://localhost:8081/swagger-ui/

### DB Config

* In-memory db --H2-- is used. It initializes the data during startup.
* The h2 console is available at `http://localhost:8081/h2-console`
* Db can be accessed at `jdbc:h2:mem:testdb`.


## store-order-service

### Running the app at local environment
store-order-service is a Spring Boot app built with maven.
The app can be built to produce a jar file. Jar file can be run as given below;

```
git clone https://github.com/ozdemirkan/store.git
cd store/
./mvnw package
java -jar store-order-service/target/store-order-service.jar
```

App can be accessed at: http://localhost:8082/
Api documentation can be accessed at: http://localhost:8082/swagger-ui/

### DB Config

* In-memory db --H2-- is used. It initializes the data during startup.
* The h2 console is available at `http://localhost:8082/h2-console`
* Db can be accessed at `jdbc:h2:mem:testdb`.


## Start the apps with Docker Compose

To create the application images, following command should be run;

```
git clone https://github.com/ozdemirkan/store.git
cd store/
./mvnw clean package -Ddocker.skip=false
```

To create containers for all apps, following command should be run;

```
docker compose up
```

Note that one should wait until all services are up and running. Then, APIs are ready to be tested

Attached [postman collection](https://raw.githubusercontent.com/ozdemirkan/store/main/Store.postman_collection.json) contains sample API calls.


## Considerations
### Authentication
OpenID Connect would be a good option for the APIs hosted on this application. By using OpenID Connect, it would be possible to verify the user identity based on authentication performed by an Authentication Server.
This allows us to decouple authentication operations from our application. 

Integration with an Authentication Server could be implemented by using Spring Security. Spring security lets us to integrate applications with an Authentication Server that supports OpenID Connect easily. 

A typical flow would be as given below;

* Rest clients retrieve two JWT tokens from Authentication Server.
  * Access token: An short living JWT token that contains information about the authenticated user.
  * Refresh token: A long living JWT Tokens that can be used to request a new Access Token without further user authentication.  
* Both access token and refresh token are  signed using a JWT signing algorithm. So, resource server doesn't need to contact with Authentication Server to verify it. It uses public keys provided by the Authentication Server for verification.   
* Each rest API call should contain an Authorization header with access token.
* When access token is expired, refresh token can be used a new access token.
* When refresh token is expired, user needs to re-authenticate with user credentials to retrieve new access token and refresh token pair.

### Redundancy
* This solution consist of 3 applications;
  * 1 api gateway
  * 2 microservices
* Each of these applications is stateless. 
* There are many alternatives for redundancy. One good alternative would be to deploy applications to a container orchestration product like Kubernetes or Openshift.
* These apps are stateless and containerized. So, they can be deployed to Kubernetes/Openshift and scaled up/down easily. This provides not only redundancy but also scalability. 
