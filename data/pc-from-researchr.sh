#!/bin/bash

set -e

if [ -z $1 ]; then
    echo "no args provided";
    exit 1;
fi

EXTERNAL="false"
URL=$1
if [ "$1" == "-e" ]; then
    EXTERNAL="true"
    if [ -z $2 ]; then
        echo "no URL provided";
        exit 1;
    fi
    URL=$2
fi

SED_PROG='s/<h3 class="media-heading">//;s/<\/h3>//;s/<small>.*//'

#echo $SED_PROG

if [ "$EXTERNAL" == "true" ]; then
    SED_PROG="$SED_PROG;s/^/E:/";
fi

curl $URL | tidy | grep "<h3 class=\"media-heading" | sed ''"$SED_PROG"''


