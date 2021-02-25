source ./env
OUTPUT=$(curl -X GET -u $USER:$PASS -H "Accepts: application/json" -H "Content-Type: application/json" --silent $BASE_URL/styles/$PROJECT_KEY/$STYLE_ID)
JQ_OUTPUT=$(echo "$OUTPUT" | jq "." 2>/dev/null)
if [ $? ]; then
   echo $OUTPUT | jq "."
else
    echo $OUTPUT
fi
