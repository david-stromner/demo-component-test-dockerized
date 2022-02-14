FROM maven:3.8.3-jdk-11-slim
RUN apt-get -o Acquire::Check-Valid-Until=false -o Acquire::Check-Date=false --allow-releaseinfo-change \
     update -y && apt-get install -y git vim systemd

ARG SUSPENDED_BOOT
ARG DEBUG_PORT
RUN echo "Suspended boot: $SUSPENDED_BOOT"
RUN echo "Debug port: $DEBUG_PORT"
ENV SUSPENDED_BOOT_ENV=$SUSPENDED_BOOT
ENV DEBUG_PORT_ENV=$DEBUG_PORT

RUN mkdir /application
WORKDIR /application

COPY /application/target /application

ENTRYPOINT java -jar -Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=$SUSPENDED_BOOT_ENV,address=*:$DEBUG_PORT_ENV /budget-service/pam-budget-service-application-0.0.1-SNAPSHOT-exec.jar