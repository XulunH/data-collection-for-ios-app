#!/bin/bash

# Configuration
KEY_PATH="./synai-test-env-key.pem"
REMOTE_HOST="root@120.26.82.225"
REMOTE_DIR="/opt/synai/data-collection"
JAR_FILE="data-collection-for-ios-app-0.0.1-SNAPSHOT.jar"
LOCAL_PORT=8085
REMOTE_PORT=8080

echo "=========================================="
echo "Starting Remote Application"
echo "=========================================="
echo ""

# Step 1: Check and shutdown any existing instance
echo "[1/3] Checking for existing instances..."
ssh -i "${KEY_PATH}" "${REMOTE_HOST}" << 'EOF'
PID=$(ps aux | grep "java -jar.*data-collection-for-ios-app-0.0.1-SNAPSHOT.jar" | grep -v grep | awk '{print $2}')

if [ -z "$PID" ]; then
    echo "✓ No existing instance found."
else
    echo "Found running instance with PID: $PID"
    echo "Shutting down existing instance..."
    kill $PID
    sleep 2
    
    # Force kill if still running
    if ps -p $PID > /dev/null 2>&1; then
        echo "Forcing shutdown..."
        kill -9 $PID
    fi
    echo "✓ Existing instance stopped."
fi
EOF

echo ""
echo "[2/3] Updating code from repository..."
ssh -i "${KEY_PATH}" "${REMOTE_HOST}" << EOF
cd ${REMOTE_DIR}
echo "Pulling latest changes..."
git pull
echo "✓ Code updated successfully."
EOF

echo ""
echo "[3/3] Starting application with SSH tunnel..."
echo "Application will be accessible at: http://localhost:${LOCAL_PORT}"
echo "Press Ctrl+C to stop the application and close the tunnel"
echo ""
echo "=========================================="
echo ""

# Step 3: Start the application with port forwarding
ssh -i "${KEY_PATH}" \
    -L ${LOCAL_PORT}:localhost:${REMOTE_PORT} \
    -t "${REMOTE_HOST}" \
    "cd ${REMOTE_DIR}/target && java -jar ${JAR_FILE}"
