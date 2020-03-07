#!/bin/bash

GRADLE_OPTS="-Xmx64m -Dorg.gradle.jvmargs='-Xmx256m -XX:MaxPermSize=64m'" ./gradlew verifyGoogleJavaFormat
