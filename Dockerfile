FROM navikt/java:11

USER root

# Curl brukes av healthcheck i docker-compose.
RUN apt-get -qq update && apt-get -qq -y install curl

USER apprunner

COPY vtp-pensjon-application/target/vtp-pensjon-application-1.0-SNAPSHOT.jar app.jar

EXPOSE 8636 8063 8060 8389 9093
