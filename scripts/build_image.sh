#! /bin/bash

. ./scripts/utils.sh

rm -rf build || echo ""

docker build -f deploy/Dockerfile -t "${CURRENT_IMAGE}" -t "${LATEST_IMAGE}" -v build:/app/build .
