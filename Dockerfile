FROM navikt/java:11

ENV JAVA_OPTS="-Dscenarios.dir=/app/model/scenarios/"
ENV DUMMYPROP=fraDockerfile

ARG JAR_FILE

USER root
# Curl brukes av healthcheck i docker-compose.
RUN apt-get -qq update && apt-get -qq -y install curl
RUN mkdir /app/lib
COPY server/target/lib/*.jar /app/lib/
COPY model/scenarios/ /app/model/scenarios/

COPY server/kafkasecurity.conf /app/
COPY server/src/main/resources/logback.xml logback.xml
COPY server/target/app.jar app.jar
COPY run-java.sh /

EXPOSE 8636 8063 8060 8389 9093

RUN chmod +x /run-java.sh