#!/bin/bash
mvn clean package -DskipTests
git add .
git commit -m "h"
git push
echo "upload complete"
