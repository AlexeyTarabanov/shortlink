FROM openjdk:11
COPY target/short-link-0.0.1-SNAPSHOT.jar /usr
WORKDIR /usr
CMD ["java", "-jar", "short-link-0.0.1-SNAPSHOT.jar"]