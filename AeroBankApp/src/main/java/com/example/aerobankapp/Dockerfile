
FROM openjdk:24-slim as builder

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set the working directory
WORKDIR /app

COPY pom.xml .
COPY src ./src

# Enable Lombok annotation processing
RUN mkdir -p /root/.m2 \
    && echo "<settings><profiles><profile><id>lombok</id><activation><activeByDefault>true</activeByDefault></activation><properties><lombok.version>1.18.30</lombok.version></properties></profile></profiles></settings>" > /root/.m2/settings.xml

RUN mvn clean package

# Use a smaller base image for the run time
FROM openjdk:24-slim

# Set the working directory
WORKDIR /app

# Copy the packaged application from the builder stage
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
