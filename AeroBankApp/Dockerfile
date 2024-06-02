
# Use a base image with JDK 11
FROM tomcat:9.0.54-jdk11-openjdk-slim

RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the WAR file to the webapps directory in tomcat
COPY target/AeroBankApp-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/AeroBankApp-0.0.1-SNAPSHOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
