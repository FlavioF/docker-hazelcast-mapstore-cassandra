docker-hazelcast-map-store-cassandra
====================================
This project creates a docker image to run Hazelcast with a Cassandra map store.

# Pre-requisites

* JDK 8
* Maven 3.1.0 or newer
* Cassandra 2.1.2
* Docker 1.5

# Build

## Clone
```
git clone https://github.com/FlavioF/docker-hazelcast-map-store-cassandra.git
cd docker-hazelcast-map-store-cassandra
```

## Build Docker image
```
./rebuild.sh
```

## Running Hazelcast Node
```
./start-hazelcast.sh
```
or
```
docker run -d -p 5701:5701 --name hazelcast1 -e "GROUP_NAME=test-group" -e "CASSANDRA_HOST=172.17.42.1" -e "GROUP_PASS=test" fferreira/hazelcast
```


## Running Multi Nodes of Hazelcast
```
./start-hazelcast-multi-nodes.sh
```

You can use docker logs to see if everything is ok.

Run this:
```
docker logs hazelcast1
```

You should expect something like this:
```
INFO: [172.17.0.53]:5701 [test-group] [3.4] Established socket connection between /172.17.0.53:5701 and 172.17.42.1/172.17.42.1:45297
Feb 11, 2015 4:13:02 PM com.hazelcast.client.impl.client.AuthenticationRequest
INFO: [172.17.0.53]:5701 [test-group] [3.4] Received auth from Connection [/172.17.0.53:5701 -> 172.17.42.1/172.17.42.1:45297], endpoint=null, live=true, type=JAVA_CLIENT, successfully authenticated
Feb 11, 2015 4:13:02 PM com.hazelcast.nio.tcp.SocketAcceptor
INFO: [172.17.0.53]:5701 [test-group] [3.4] Accepting socket connection from /172.17.42.1:45298
Feb 11, 2015 4:13:02 PM com.hazelcast.nio.tcp.TcpIpConnectionManager
INFO: [172.17.0.53]:5701 [test-group] [3.4] Established socket connection between /172.17.0.53:5701 and 172.17.42.1/172.17.42.1:45298
Feb 11, 2015 4:13:02 PM com.hazelcast.client.impl.client.AuthenticationRequest
INFO: [172.17.0.53]:5701 [test-group] [3.4] Received auth from Connection [/172.17.0.53:5701 -> 172.17.42.1/172.17.42.1:45298], endpoint=null, live=true, type=JAVA_CLIENT, successfully authenticated
Feb 11, 2015 4:13:02 PM com.hazelcast.partition.InternalPartitionService
INFO: [172.17.0.53]:5701 [test-group] [3.4] Initializing cluster partition table first arrangement...
Feb 11, 2015 4:13:04 PM com.hazelcast.cluster.ClusterService
INFO: [172.17.0.53]:5701 [test-group] [3.4] 

Members [3] {
        Member [172.17.0.53]:5701 this
        Member [172.17.0.54]:5701
        Member [172.17.0.55]:5701
}

Feb 11, 2015 4:13:05 PM com.hazelcast.partition.InternalPartitionService
INFO: [172.17.0.53]:5701 [test-group] [3.4] Re-partitioning cluster data... Migration queue size: 181
Feb 11, 2015 4:13:06 PM com.hazelcast.partition.InternalPartitionService
INFO: [172.17.0.53]:5701 [test-group] [3.4] All migration tasks have been completed, queues are empty.
Feb 11, 2015 4:13:16 PM com.hazelcast.nio.tcp.TcpIpConnection
INFO: [172.17.0.53]:5701 [test-group] [3.4] Connection [Address[172.17.42.1]:45297] lost. Reason: java.io.EOFException[Remote socket closed!]
Feb 11, 2015 4:13:16 PM com.hazelcast.nio.tcp.ReadHandler
```

## Running a client
Running a client to test if Hazelcast Client can get the Hazelcast Instance and to test if it is storing the data in Cassandra

```
mvn test
```

### Validating results
```
cqlsh> use example;
cqlsh:example> select * from hzentry;

 id                                                    | data
-------------------------------------------------------+-----------------------------------------------------------------------
 user_cassamdra_2_a09beb30-1485-4f55-b92d-c79445f7b26d | {"firtName":"Flávio2","lastName":"Ferreira2","country":"Portugal"}
 user_cassamdra_1_815ff0b6-fb4a-431d-b1b1-6dc5aaf35e5a | {"firtName":"Flávio1","lastName":"Ferreira1","country":"Portugal"}

```
