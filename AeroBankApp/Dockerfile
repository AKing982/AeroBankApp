# Build stage
FROM maven:3.8.2-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY . .
RUN mvn clean package -DskipTests

# Runtime stage
FROM tomcat:9.0-jdk11-openjdk
WORKDIR /usr/local/tomcat/webapps
COPY --from=builder /app/target/*.war app.war
EXPOSE 8080
ENTRYPOINT ["java", "-cp", "/usr/local/tomcat/webapps/app.war:/usr/local/tomcat/lib/*", "com.example.aerobankapp.AeroBankAppApplication"]
CMD ["catalina.sh", "run"]