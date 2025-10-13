#!/bin/sh

export SPRING_PROFILES_ACTIVE=dev
./gradlew clean
./gradlew bootRun
