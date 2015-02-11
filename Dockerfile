# Name: Hazelcast v3.3 Dockerfile
# Dockerfile Version: v0.0.1
# Hazelcast Version: v3.3
# Dependencies (one of this): 
# 	- ubuntu:14.04
# Volumes: Log (/var/log)

FROM ubuntu:14.04
MAINTAINER Flávio Ferreira <flaviommferreira@gmail.com>

#UPDATE OS
RUN apt-get update
RUN apt-get upgrade -y
RUN apt-get install -y software-properties-common

RUN echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections
RUN echo debconf shared/accepted-oracle-license-v1-1 seen true | debconf-set-selections

# Install JAVA8
RUN add-apt-repository -y ppa:webupd8team/java
RUN apt-get update
RUN apt-get install -y oracle-java8-installer
RUN apt-get autoclean

RUN mkdir -p /opt/hazelcast
WORKDIR /opt/hazelcast

# Copying files to docker image
ADD target/hazelcast-map-store-0.1-SNAPSHOT-jar-with-dependencies.jar /opt/hazelcast/hazelcast-map-store-0.1-SNAPSHOT-jar-with-dependencies.jar
ADD src/InstanceStart.java /opt/hazelcast/InstanceStart.java
ADD src/start.sh /opt/hazelcast/start.sh

RUN chmod 755 /opt/hazelcast/start.sh
RUN javac -cp "hazelcast-map-store-0.1-SNAPSHOT-jar-with-dependencies.jar" InstanceStart.java

EXPOSE 5701

CMD /opt/hazelcast/start.sh