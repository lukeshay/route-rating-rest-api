#!/bin/bash

rm -rf build
rm -rf rest-api.jar
rm -rf restapi.log

#./gradlew clean build --no-daemon  \
#    -x jacocoTestReport \
#    -x test \
#    -x verifyGoogleJavaFormat \
#    -x compileTestJava \
#    -x processTestResources \
#    -x testClasses \
#    -x bootDistTar \
#    -x bootDistZip \
#    -x distTar \
#    -x distZip \
#    -x startScripts \
#    -x check \
#    || exit 1

GRADLE_OPTS="-Xmx64m -Dorg.gradle.jvmargs='-Xmx256m -XX:MaxPermSize=64m'" ./gradlew build \
  --no-rebuild \
  -x verifyGoogleJavaFormat \
  -x bootDistTar \
  -x bootDistZip \
  -x distTar \
  -x distZip \
  -x startScripts

mv build/libs/*.jar rest-api.jar
