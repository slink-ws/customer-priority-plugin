source ./env

OUTPUT=$(curl -u $USER:$PASS -H "Accepts: application/json" --silent $BASE_URL/viewers/$PROJECT_KEY)
JQ_OUTPUT=$(echo "$OUTPUT" | jq -r ".")
STATUS=$?
if [ "0" == "$STATUS" ]; then
#   echo "JQ OUT"
   echo $OUTPUT | jq "."
else
    echo "RAW OUT"
    echo $OUTPUT
fi
