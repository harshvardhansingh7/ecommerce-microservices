FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/notification-service-1.0.0.jar notification-service.jar
ENTRYPOINT ["java", "-jar", "/notification-service.jar"]