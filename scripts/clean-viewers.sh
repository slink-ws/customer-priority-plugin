#!/bin/bash

source ./env
OUTPUT=$(curl -X DELETE -u $USER:$PASS --silent $BASE_URL/viewers/$PROJECT_KEY)
JQ_OUTPUT=$(echo "$OUTPUT" | jq "." 2>/dev/null)
if [ $? ]; then
   echo $OUTPUT | jq "."
else
    echo $OUTPUT
fi
