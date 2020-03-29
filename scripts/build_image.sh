#! /bin/bash

. ./scripts/utils.sh

docker build -f deploy/Dockerfile -t "${CURRENT_IMAGE}" -t "${LATEST_IMAGE}" .

rm -rf rest-api.jar
