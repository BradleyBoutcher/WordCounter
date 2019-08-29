#!/usr/bin/env bash

java -Xdebug \
    -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=1044 \
    -Dfile.encoding=UTF-8 \
    -jar ./target/wordcounter-1.0-SNAPSHOT.jar
