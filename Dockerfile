FROM adoptopenjdk/openjdk15:alpine

VOLUME /tmp
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","target/technology-api-0.0.1.jar"]