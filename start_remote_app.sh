#!/bin/bash

# === CONFIGURATION ===
KEY_PATH="./synai-test-env-key.pem"
REMOTE_USER="root"
REMOTE_HOST="120.26.82.225"
JAR_PATH="/opt/synai/data-collection-for-ios-app-0.0.1-SNAPSHOT.jar"
LOCAL_PORT=8001
REMOTE_PORT=8080

# === STEP 1: Start the app on the remote server ===
echo "Starting remote app on $REMOTE_HOST..."
ssh -i "$KEY_PATH" "$REMOTE_USER@$REMOTE_HOST" "
  echo 'Running JAR on remote server...'
  nohup java -jar '$JAR_PATH' > /opt/synai/app.log 2>&1 &
  echo 'App started on remote server.'
  exit
"

# === STEP 2: Create SSH tunnel ===
echo "Creating local port forward: localhost:$LOCAL_PORT → $REMOTE_HOST:$REMOTE_PORT"
ssh -i "$KEY_PATH" -L "$LOCAL_PORT:localhost:$REMOTE_PORT" "$REMOTE_USER@$REMOTE_HOST"
