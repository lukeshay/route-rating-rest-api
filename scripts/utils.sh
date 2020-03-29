#!/bin/bash

IMAGE_NAME=lukeshaydocker/$(basename "$(git rev-parse --show-toplevel)")
TAG=$(git rev-parse --short HEAD)$(git diff --quiet || echo ".uncommitted")

LATEST_IMAGE="${IMAGE_NAME}:latest"
CURRENT_IMAGE="${IMAGE_NAME}:${TAG}"
