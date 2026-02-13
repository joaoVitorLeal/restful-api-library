##### BUILD #####
FROM maven:3.9.9-amazoncorretto-21-al2023 AS build
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

##### RUN #####
# IMAGE OF AMAZON JDK-21
FROM amazoncorretto:21.0.5
WORKDIR /app
COPY --from=build ./build/target/*.jar ./libraryapi.jar

# OF APPLICATION
EXPOSE 8080
# OF ACTUATOR
EXPOSE 9090

ENV DATASOURCE_URL=''
ENV DATASOURCE_USERNAME=''
ENV DATASOURCE_PASSWORD=''
ENV GOOGLE_CLIENT_ID='client_id'
ENV GOOGLE_CLIENT_SECRET='client_secret'

ENV SPRING_PROFILES_ACTIVE='production'

ENV TZ='America/Sao_Paulo'

ENTRYPOINT ["java", "-jar", "libraryapi.jar"]
