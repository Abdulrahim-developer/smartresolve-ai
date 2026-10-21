# STAGE 1: COMPILATION & PACKAGING ENVIRONMENT
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build_stage
WORKDIR /app

# Copy dependency configuration files first to utilize Docker layer caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code tree and compile production JAR asset bypassing test loops
COPY src ./src
RUN mvn clean package -DskipTests

# STAGE 2: HIGH-PERFORMANCE RUNTIME ENGINE
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Pull only the compiled executable binary out of the build stage
COPY --from=build_stage /app/target/smartresolve-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
