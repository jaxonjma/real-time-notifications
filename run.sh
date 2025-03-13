#!/bin/bash

echo "Iniciando servicios..."
./gradlew :game-events-service:bootRun &
./gradlew :notification-service:bootRun &
./gradlew :api-gateway:bootRun &

wait