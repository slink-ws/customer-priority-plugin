source ./env
OUTPUT=$(curl -X POST -u $USER:$PASS -H "Accepts: application/json" -H "Content-Type: application/json" -d @style1.json --silent $BASE_URL/$PROJECT_KEY)
JQ_OUTPUT=$(echo "$OUTPUT" | jq "." 2>/dev/null)
if [ $! ]; then
   echo $JQ_OUTPUT
else
    echo $OUTPUT
fi
