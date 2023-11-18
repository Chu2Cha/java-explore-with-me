FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar stat-service.jar
ENTRYPOINT ["java","-jar","/stat-service.jar"]