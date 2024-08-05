FROM tomcat
COPY target/01tuanthietke-0.0.1-SNAPSHOT.jar ./01tuanthietke-0.0.1-SNAPSHOT.jar
USER root
RUN apt-get update && \
    apt-get install -y ffmpeg && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* 
ENTRYPOINT ["java","-jar","./01tuanthietke-0.0.1-SNAPSHOT.jar"]

    
