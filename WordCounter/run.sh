#!/usr/bin/env bash

## Quick commands for running in interactive mode

case $1 in
  start)
    java -jar target/wordcounter-1.0-SNAPSHOT.jar \
  ;;

  debug)
    java -Xdebug \
        -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=1044 \
        -Dfile.encoding=UTF-8 \
        -jar ./target/wordcounter-1.0-SNAPSHOT.jar
  ;;

  help)
    echo "Command must be one of the following: "
    echo "  Start"
    echo "  Debug"
    echo "  Help"
    ;;

  *)
    echo "Command must be one of the following: "
    echo "  Start"
    echo "  Debug"
    echo "  Help"
    exit 1
    ;;

esac

: 0
