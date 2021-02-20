source ./env
OUTPUT=$(curl -X PUT -u $USER:$PASS -H "Accepts: application/json" -H "Content-Type: application/json" -d @style2.json --silent $BASE_URL/$PROJECT_KEY)
JQ_OUTPUT=$(echo "$OUTPUT" | jq "." 2>/dev/null)
if [ $? ]; then
   echo $OUTPUT | jq "."
else
    echo $OUTPUT
fi
