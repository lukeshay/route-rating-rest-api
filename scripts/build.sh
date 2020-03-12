#!/bin/sh

rm -rf build || echo ""
rm -rf rest-api.jar || echo ""
rm -rf restapi.log || echo ""

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

./gradlew build \
  --no-rebuild \
  -x verifyGoogleJavaFormat \
  -x bootDistTar \
  -x bootDistZip \
  -x distTar \
  -x distZip \
  -x startScripts

mv build/libs/*.jar rest-api.jar
