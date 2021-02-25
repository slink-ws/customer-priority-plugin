source ./env
curl -X DELETE -u $USER:$PASS -H "Accepts: application/json" --silent $BASE_URL/styles/$PROJECT_KEY
