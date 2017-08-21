FROM openjdk:8-jdk-alpine

WORKDIR /opt

ENV RABBIT_HOST=localhost
ENV RABBIT_PORT=5672

# copy in the gradlew, gradle credentials and src folder
ADD gradle ./gradle
ADD src ./src

COPY gradlew gradle.properties.enc build.gradle ./
# build the code
RUN ./gradlew assemble

CMD java -jar build/libs/*.jar --spring.rabbitmq.host=$RABBIT_HOST --spring.rabbitmq.port=$RABBIT_PORT
