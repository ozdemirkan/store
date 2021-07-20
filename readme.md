# STORE APPLICATION

Store app is a parent project which contains;
* An api-gateway using [Spring Cloud Gateway](https://https://github.com/spring-cloud/spring-cloud-gateway).
  * store-gateway-service
* Two microservices developed using [Spring Boot](https://github.com/spring-projects/spring-boot).
    * store-product-service
    * store-order-service

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

### DB Config

* In-memory db --H2-- is used. It initializes the data during startup.
* The h2 console is available at `http://localhost:8082/h2-console`
* Db can be accessed at `jdbc:h2:mem:testdb`.