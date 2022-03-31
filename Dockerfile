FROM eclipse-temurin:11 as builder
ARG JAR_FILE=vtp-pensjon-application/target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM eclipse-temurin:11
ENV TZ="Europe/Oslo"
COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./

EXPOSE 8636 8063 8060 8389 9093

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
