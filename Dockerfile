FROM navikt/java:11

ENV JAVA_OPTS="-Dscenarios.dir=/app/model/scenarios/"

ARG JAR_FILE

# Curl brukes av healthcheck i docker-compose.
RUN apt-get -qq update && apt-get -qq -y install curl
RUN mkdir /app/lib
COPY server/target/lib/*.jar /app/lib/
COPY model/scenarios/ /app/model/scenarios/
RUN chmod -R 777 /app/model/scenarios/

COPY server/src/main/resources/logback.xml logback.xml
COPY server/target/app.jar app.jar

RUN mkdir /root/.modig
COPY truststore.jks /root/.modig/
COPY keystore.jks /root/.modig/

COPY run-java.sh /

EXPOSE 8636 8063 8060 8389 9093

RUN chmod +x /run-java.sh
