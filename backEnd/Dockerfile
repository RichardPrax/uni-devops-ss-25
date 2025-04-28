# Build Image - referencable by build
FROM maven:3.9-eclipse-temurin-21 as build
WORKDIR /workspace/app

# COPY POM
COPY pom.xml pom.xml

# COPY sources
COPY src src

# Jar packen
RUN mvn package -DskipTests

# Final image
FROM eclipse-temurin:21-jre
ARG DEPENDENCY=/workspace/app/target

# Copy build results
WORKDIR /app
COPY --from=build ${DEPENDENCY}/koerperschmiede-api.jar .

# Specify listening port
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "koerperschmiede-api.jar"]