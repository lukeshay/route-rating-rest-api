#!/bin/bash

. ./scripts/utils.sh

if [[ "${PUSH_LATEST}" == "TRUE" ]]; then
    echo "Pushing ${LATEST_IMAGE}"
    docker push "${LATEST_IMAGE}"
fi

echo "Pushing ${CURRENT_IMAGE}"
docker push "${CURRENT_IMAGE}"
