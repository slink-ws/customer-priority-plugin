INPUT_FILE=$1
if [ -z "$INPUT_FILE" ]; then
    INPUT_FILE=style1-upd.json
fi

source ./env
OUTPUT=$(curl -X PUT -u $USER:$PASS -H "Accepts: application/json" -H "Content-Type: application/json" -d @style1-upd.json --silent $BASE_URL/styles/$PROJECT_KEY)
JQ_OUTPUT=$(echo "$OUTPUT" | jq "." 2>/dev/null)
if [ $? ]; then
   echo $OUTPUT | jq "."
else
    echo $OUTPUT
fi
