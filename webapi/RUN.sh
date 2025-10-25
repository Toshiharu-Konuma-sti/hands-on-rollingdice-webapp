#!/bin/sh

echo "Test URL:"
echo "- http://localhost:8182/api/dice/v1/roll"
echo "- http://localhost:8182/api/dice/v1/list" 

export SPRING_PROFILES_ACTIVE=dev
./gradlew clean
./gradlew bootRun
