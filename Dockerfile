FROM java:8
RUN mkdir /lecture
WORKDIR /lecture
COPY build/distributions/java-evaluation-service.tar service.tar
RUN tar -xf service.tar
EXPOSE 8080
ENTRYPOINT service/bin/java-evaluation-service
