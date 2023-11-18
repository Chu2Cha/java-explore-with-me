FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar stat-client.jar
ENTRYPOINT ["java","-jar","/stat-client.jar"]