#!/bin/bash
docker stop $(docker ps | grep -i 'neo4j:latest' | awk {'print $1'})
docker run  -p7474:7474 -p7687:7687 -d --env NEO4J_AUTH=neo4j/test neo4j:latest 
cd ./hackster
./gradlew clean build
java -jar ./build/libs/hackster-0.0.1-SNAPSHOT.jar --spring.config.location=./src/main/resources/application.properties