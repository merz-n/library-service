FROM openjdk:21
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
# профиль docker использует application-docker.yml
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT ["java","-jar","/app/app.jar"]