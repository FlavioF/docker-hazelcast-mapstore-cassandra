#!/usr/bin/env bash
# Script to start hazelcast on docker

# Here we eval if some variables exists on the environment if they dont then we set them
MIN_HEAP=${MIN_HEAP:=1G}
MAX_HEAP=${MAX_HEAP:=1G}
GROUP_NAME=${GROUP_NAME=dev}
GROUP_PASS=${GROUP_PASS=devpass}
# Add docker gateway by default for the case of cassandra running in localhost
CASSANDRA_HOST=${CASSANDRA_HOST=172.17.42.1}

java -Xms$MIN_HEAP -Xmx$MAX_HEAP -Djava.net.preferIPv4Stack=true -cp "/opt/hazelcast/hazelcast-map-store-0.1-SNAPSHOT-jar-with-dependencies.jar:/opt/hazelcast/" InstanceStart $GROUP_NAME $GROUP_PASS $CASSANDRA_HOST