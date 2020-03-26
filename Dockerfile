FROM openjdk:8
ADD ./target/PatientDocumentSearchApp-1.0.jar app.jar
EXPOSE 8080
CMD /usr/bin/java -Xmx400m -Xms400m -jar app.jar
#ENTRYPOINT ["java", "-jar", "app.jar"]
