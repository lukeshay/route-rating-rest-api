#! /bin/bash

IMAGE_NAME=lukeshaydocker/$(shell basename "$(git rev-parse --show-toplevel)")
TAG=$(shell git rev-parse --short HEAD)$(shell git diff --quiet || echo ".uncommitted")

if [[ "${SKIP_BUILD}" != "TRUE" ]]; then
    ./scripts/build.sh || exit 1
fi

docker build -f deploy/Dockerfile -t "${IMAGE_NAME}":"${TAG}" -t "${IMAGE_NAME}":latest .
