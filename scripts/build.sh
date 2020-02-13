#!/bin/bash
rm -rf build
rm -rf rest-api.jar
rm -rf restapi.log

./gradlew clean build --no-daemon \
    -x jacocoTestReport \
    -x test \
    -x verifyGoogleJavaFormat \
    -x compileTestJava \
    -x processTestResources \
    -x testClasses \
    -x bootDistTar \
    -x bootDistZip \
    -x distTar \
    -x distZip \
    -x startScripts \
    -x check \
    || exit 1

mv build/libs/*.jar rest-api.jar || exit 1
