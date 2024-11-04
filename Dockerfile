FROM openjdk:17-bullseye


WORKDIR /code/


RUN apt-get update && apt-get install -y curl && apt-get clean


ARG NEXUS_USER=admin
ARG NEXUS_PASSWORD=nexus
ARG NEXUS_REPO_URL=http://192.168.172.144:8081/repository/maven-releases
ARG JAR_PATH=tn/esprit/tp-foyer/5.0.0/tp-foyer-5.0.0.jar


RUN curl -u $NEXUS_USER:$NEXUS_PASSWORD \
    -O $NEXUS_REPO_URL/$JAR_PATH

#1
RUN rm -rf /root/.cache /tmp/* /var/tmp/*

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JHIPSTER_SLEEP=0 \
    JAVA_OPTS="" \
    PHASE=""


EXPOSE 8089


CMD java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar tp-foyer-5.0.0.jar
