#!/bin/bash

REPORTER=$1

if [ -z "$REPORTER" ]; then
   echo "REPORTER not set"
   exit 1
fi

source ./env
OUTPUT=$(curl -X DELETE -u $USER:$PASS --silent $BASE_URL/$PROJECT_KEY/$STYLE_ID/reporter?reporter=$REPORTER)
JQ_OUTPUT=$(echo "$OUTPUT" | jq "." 2>/dev/null)
if [ $? ]; then
   echo $OUTPUT | jq "."
else
    echo $OUTPUT
fi
