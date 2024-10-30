FROM openjdk:17-bullseye

WORKDIR /app/

RUN apt-get update && apt-get install -y curl && apt-get clean

ARG NEXUS_USER=admin
ARG NEXUS_PASSWORD=Asmach123+
ARG NEXUS_REPO_URL=http://192.168.50.4:8081/repository/maven-releases
ARG JAR_PATH=tn/esprit/tp-foyer/5.0.0/tp-foyer-5.0.0.jar

RUN curl -u $NEXUS_USER:$NEXUS_PASSWORD \
    -O $NEXUS_REPO_URL/$JAR_PATH \

RUN rm -rf /root/.cache /tmp/* /var/tmp/*

EXPOSE 8089

# Exécuter l'application Spring Boot
CMD ["java", "-jar", "/app/tp-foyer-5.0.0.jar"]
