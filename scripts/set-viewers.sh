INPUT_FILE=$1
if [ -z "$INPUT_FILE" ]; then
  INPUT_FILE="viewers.json"
fi
source ./env
OUTPUT=$(curl -X POST -u $USER:$PASS -H "Accepts: application/json" -H "Content-Type: application/json" -d @$INPUT_FILE --silent $BASE_URL/viewers/$PROJECT_KEY)
JQ_OUTPUT=$(echo "$OUTPUT" | jq "." 2>/dev/null)
if [ $! ]; then
   echo $JQ_OUTPUT
else
    echo $OUTPUT
fi
