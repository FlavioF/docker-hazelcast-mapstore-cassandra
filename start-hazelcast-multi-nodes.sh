#!/bin/bash

docker run -d -p 5701:5701 --name hazelcast1 -e "GROUP_NAME=test-group" -e "GROUP_PASS=test" fferreira/hazelcast
docker run -d --name hazelcast2 -e "GROUP_NAME=test-group" -e "GROUP_PASS=test" fferreira/hazelcast
docker run -d --name hazelcast3 -e "GROUP_NAME=test-group" -e "GROUP_PASS=test" fferreira/hazelcast
