#!/bin/sh

# Check if Google Json Service file env is set, exit if not
if [ -z "$GOOGLE_SERVICES_JSON" ]; then
  echo "GOOGLE_SERVICES_JSON env variable is empty. Exiting."
  exit 1
fi

# Export to google service json file
echo $GOOGLE_SERVICES_JSON > app/google-services.json

