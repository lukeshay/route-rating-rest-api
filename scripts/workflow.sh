#!/bin/bash

echo "Building and testing"
./scripts/build.sh

echo "Pushing image"
./scripts/push_image.sh

echo "Publishing coverage"
./scripts/coverage.sh