FROM openjdk:8-jre-alpine
ADD ./target/PatientDocumentSearchApp-1.0.jar app.jar
COPY sleep_appjar.sh sleep_appjar.sh
EXPOSE 8080
ENTRYPOINT ["/bin/sh", "sleep_appjar.sh"]
