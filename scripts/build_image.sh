#! /bin/bash

. ./scripts/utils.sh

if [[ "${SKIP_BUILD}" != "TRUE" ]]; then
  . ./scripts/build.sh || exit 1
fi

docker build -f deploy/Dockerfile -t "${CURRENT_IMAGE}" -t "${LATEST_IMAGE}" .

rm -rf rest-api.jar
