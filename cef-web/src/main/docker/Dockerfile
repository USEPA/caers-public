FROM openjdk:8-jre-alpine

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    SLEEP_TIME=0 \
    JAVA_OPTS=""

# Add a cef user to run our application so that it doesn't need to run as root
RUN adduser -D -s /bin/sh cef
WORKDIR /home/cef

ADD entrypoint.sh entrypoint.sh
RUN chmod 755 entrypoint.sh && chown cef:cef entrypoint.sh
USER cef

ENTRYPOINT ["./entrypoint.sh"]

EXPOSE 8080 5701/udp

ADD *.war app.war