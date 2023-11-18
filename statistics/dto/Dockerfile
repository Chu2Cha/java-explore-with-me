FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar stat-dto.jar
ENTRYPOINT ["java","-jar","/stat-dto.jar"]