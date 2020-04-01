#!/bin/bash

. ./scripts/build_image.sh

docker-compose -f deploy/docker-compose.dev.yml up