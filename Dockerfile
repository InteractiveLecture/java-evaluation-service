FROM java:8
RUN mkdir /lecture
WORKDIR /lecture
COPY build/distributions/java-evaluation-service.tar java-evaluation-service.tar
RUN tar -xf java-evaluation-service.tar
EXPOSE 8080
CMD java-evaluation-service/bin/java-evaluation-service
