FROM openjdk:17-bullseye

#COPY . /code/foyernour/

WORKDIR /code/foyernour/
#RUN rm -rf target

# Install Maven
#RUN apt-get update
#RUN apt-get install -y maven

# Clean and package the application
#RUN mvn clean package -DskipTests --no-transfer-progress -B

# Copy the built JAR file from Jenkins workspace to the image
#RUN mv /code/foyernour/target/*.jar /code/
COPY target/*.jar /code/foyernour/app.jar


# Clean up to reduce image size
RUN rm -rf /code/foyernour/ /root/.m2 /root/.cache /tmp/* /var/tmp/*

# Set environment variables
ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JHIPSTER_SLEEP=0 \
    JAVA_OPTS="" \
    PHASE=""

# Expose the application port
EXPOSE 8094

# Command to run the application
CMD java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /code/*.jar
