FROM adoptopenjdk/openjdk11:alpine
#FROM openjdk:12-ea-29-jdk-alpine3.9
#FROM adoptopenjdk/openjdk11
VOLUME /tmp

ADD ./target/purchasing-0.0.1-SNAPSHOT.jar purchasing.jar
# ADD ./keystore.jks keystore.jks

# Expose 8220, the default port used for Capacity Service
EXPOSE 8220
ENV JAVA_OPTS=""
RUN ["apk", "update"]
RUN ["apk", "add", "tzdata"]
RUN ["ln", "-f", "-s", "/usr/share/zoneinfo/Europe/London", "/etc/localtime"]
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=docker -Djava.security.egd=file:/dev/./urandom -jar purchasing.jar" ]
