#!/bin/sh

rm -rf build || echo ""
rm -rf rest-api.jar || echo ""
rm -rf restapi.log || echo ""

./gradlew build \
    --no-rebuild \
    -x verifyGoogleJavaFormat \
    -x bootDistTar \
    -x bootDistZip \
    -x distTar \
    -x distZip \
    -x startScripts || exit 1

mv build/libs/*.jar rest-api.jar
