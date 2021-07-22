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