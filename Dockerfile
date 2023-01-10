FROM openjdk:8-jre-alpine
MAINTAINER hoadp

ADD ./target/customer-service.jar /app/
CMD ["java", "-Xmx2048m", "-jar", "/app/customer-service.jar"]

EXPOSE 9022