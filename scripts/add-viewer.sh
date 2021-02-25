#!/bin/bash

VIEWER=$1

if [ -z "$VIEWER" ]; then
   echo "VIEWER not set"
   exit 1
fi

source ./env
OUTPUT=$(curl -X PUT -u $USER:$PASS  --silent $BASE_URL/viewers/$PROJECT_KEY?viewer=$VIEWER)
JQ_OUTPUT=$(echo "$OUTPUT" | jq "." 2>/dev/null)
if [ $? ]; then
   echo $OUTPUT | jq "."
else
    echo $OUTPUT
fi
