#!/bin/bash
./gradlew clean build
docker build -t openservice/java-evaluation-service:latest .
if [ "${TRAVIS_PULL_REQUEST}" = "false" ] && [ "${TRAVIS_REPO_SLUG}" = "InteractiveLecture/java-evaluation-service" ] ; then
  docker login -u="$DOCKER_USERNAME" -p="$DOCKER_PASSWORD" -e="$DOCKER_EMAIL"
  docker push openservice/java-evaluation-service:latest
fi
