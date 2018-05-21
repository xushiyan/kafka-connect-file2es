FROM openjdk:jdk-alpine

LABEL maintainer "rxu"

ENV KAFKA_VERSION "0.9.0.1"
ENV DATA_SOURCES_DIR "data-sources"
ENV WORKERS_DIR "worker-props"

RUN apk update && apk add bash curl \
    && rm -rf /var/cache/apk

WORKDIR /opt
RUN curl https://archive.apache.org/dist/kafka/${KAFKA_VERSION}/kafka_2.11-${KAFKA_VERSION}.tgz | tar xz
ADD . .
RUN javac WorkerPropertiesGenerator.java

CMD /bin/sh -c "java WorkerPropertiesGenerator && \
                /bin/bash kafka_2.11-${KAFKA_VERSION}/bin/connect-standalone.sh \
                connect-standalone.docker.properties \
                ${WORKERS_DIR}/*"
