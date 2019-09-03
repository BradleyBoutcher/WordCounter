#!/usr/bin/env bash

case $1 in
  start)
    java -jar target/wordcounter-1.0-SNAPSHOT.jar \
        "$@"
  ;;

  debug)
    java -Xdebug \
        -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=1044 \
        -Dfile.encoding=UTF-8 \
        -jar ./target/wordcounter-1.0-SNAPSHOT.jar
        "$@"
  ;;

  help)
    echo "Option must be one of the following"
    echo "  headless"
    echo "  debug"
    echo "  help"
    ;;

  *)
    echo -e parameter must be one of "headless, debug, or help"
    exit 1
    ;;

esac

: 0
