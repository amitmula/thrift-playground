#!/bin/bash

if [ "$1" != "" ]
then
   ~/maven-3/bin/mvn clean install
fi

java  -cp $HADOOP_CLASSPATH:target/SentryClient-1.0-SNAPSHOT-jar-with-dependencies.jar  com.impetus.idw.sentry.SentryJDBCClient
