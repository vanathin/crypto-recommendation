# Start with a base image containing Java runtime
eclipse-temurin:17-jdk

# Add Maintainer Info
LABEL maintainer="vanathi.nallasivam@gmail.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8080

# The application's jar file
ARG JAR_FILE=target/crypto-recommendation.jar

# Add the application's jar to the container
ADD ${JAR_FILE} app.jar

# Run the jar file
ENTRYPOINT ["java","-jar","/app.jar"]
