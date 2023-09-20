#!/bin/bash
HEALTH_ENDPOINT="$1"
MAX_ATTEMPTS=20
SLEEP_INTERVAL=2

if [ $# -eq 0 ]; then
  echo "Error: No HEALTH_ENDPOINT provided as an argument."
  exit 1
fi

# Loop until a 200 status code is received or max attempts reached
for ((i=1; i<=$MAX_ATTEMPTS; i++)); do
  STATUS_CODE=$(curl -o /dev/null -w '%{http_code}' -s $HEALTH_ENDPOINT)
  if [ $STATUS_CODE -eq 200 ]; then
    echo "Health check successful (HTTP 200 OK)"
    break
  else
    echo "Health check failed (HTTP $STATUS_CODE)"
    if [ $i -lt $MAX_ATTEMPTS ]; then
      echo "Retrying in $SLEEP_INTERVAL seconds..."
      sleep $SLEEP_INTERVAL
    else
      echo "Max attempts reached. Exiting with failure."
      exit 1
    fi
  fi
done