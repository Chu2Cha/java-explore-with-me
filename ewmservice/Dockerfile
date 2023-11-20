FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar main-svc.jar
ENTRYPOINT ["java","-jar","/main-svc.jar"]