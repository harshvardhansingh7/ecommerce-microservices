FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/cart-service-1.0.0.jar cart-service.jar
ENTRYPOINT ["java", "-jar", "/cart-service.jar"]