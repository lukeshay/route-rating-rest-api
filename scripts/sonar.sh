#!/bin/sh

sonar-scanner \
  -Dsonar.login=86df8f3836abaa380b488d38c387c9a8ae870b1b \
  -Dsonar.branch.name=$2 \
  -Dsonar.projectKey=$3 \
  -Dsonar.projectVersion=$4 \
  -Dsonar.organization=luke-shay \
  -Dsonar.sources=src/main/java/ \
  -Dsonar.exclusions=**/*Repository.class,**/*Repository.java,**/*Body.class,**/*Body.java,**/*View.class,**/*View.java,**/*Payload.class,**/*Payload.java,**/*Filter.class,**/*Filter.java,**/*Exception.class,**/*Exception.java,**/*SpringMain.class,**/*SpringMain.java \
  -Dsonar.tests=src/test/java/ \
  -Dsonar.java.source=11 \
  -Dsonar.java.target=11 \
  -Dsonar.java.binaries=build/classes/java/main/ \
  -Dsonar.java.test.binaries=build/classes/java/test/ \
  -Dsonar.junit.reportPaths=build/test-results/test/
