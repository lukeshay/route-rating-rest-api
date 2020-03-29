#!/bin/sh

rm -rf build || echo ""
rm -rf rest-api.jar || echo ""
rm -rf rest-api.log || echo ""

./gradlew build \
  --no-rebuild \
  -x bootDistTar \
  -x bootDistZip \
  -x distTar \
  -x distZip \
  -x startScripts

mv build/libs/*.jar rest-api.jar
