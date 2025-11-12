FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/payment-service-1.0.0.jar payment-service.jar
ENTRYPOINT ["java", "-jar", "/payment-service.jar"]