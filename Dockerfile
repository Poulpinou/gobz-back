FROM adoptopenjdk/openjdk11:alpine-jre

# [Labels]
LABEL maintainer="donovan.persent@gmail.com"
LABEL description="Gobz API"


# [Settings]
ARG JAR_FILE=target/gobz-api.jar


# [Actions]
WORKDIR /opt/app

COPY ${JAR_FILE} app.jar


# [Entry point]
ENTRYPOINT ["java","-jar","app.jar"]