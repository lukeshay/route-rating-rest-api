#!/bin/bash

rm -rf build output.yml

if [[ "${SKIP_BUILD}" != "TRUE" ]]; then
    docker build -f deploy/Dockerfile.lambda -t rest-api-lambda .
    docker run -v "${PWD}"/build:/app/build rest-api-lambda
fi

if [[ "${SKIP_PACKAGE}" != "TRUE" ]]; then
    sam package \
        --template-file template.yml \
        --s3-bucket route-rating-rest-api-builds \
        --s3-prefix "$(git rev-parse --short HEAD)" \
        --output-template-file output.yml \
        --region us-east-2
fi

if [[ "${SKIP_DEPLOY}" != "TRUE" ]]; then
    sam deploy \
        --s3-bucket route-rating-rest-api-builds \
        --template-file output.yml \
        --region us-east-2 \
        --no-confirm-changeset \
        --stack-name "$(basename "$(git rev-parse --show-toplevel)")" \
        --capabilities CAPABILITY_IAM
fi

FUNCTION_NAME=$(
    aws lambda list-functions |
        grep '"FunctionName": "route-rating-rest-api-RestApiFunction' |
        sed 's/"FunctionName": "//g' |
        sed 's/",//g' |
        sed 's/ //g'
)

ENV_VARS="{
  GOOGLE_RECAPTCHA_TOKEN='${GOOGLE_RECAPTCHA_TOKEN}',
  REST_API_DB_PASSWORD='${REST_API_DB_PASSWORD}',
  DIGITAL_OCEAN_SECRET_KEY=${DIGITAL_OCEAN_SECRET_KEY},
  DIGITAL_OCEAN_ACCESS_KEY=${DIGITAL_OCEAN_ACCESS_KEY},
  REST_API_DB_URL=${REST_API_DB_URL},
  REST_API_JWT_SECRET=${REST_API_JWT_SECRET},
  REST_API_REFRESH_SECRET=${REST_API_REFRESH_SECRET}
}"

aws lambda \
    update-function-configuration \
    --function-name "${FUNCTION_NAME}" \
    --environment "Variables=${ENV_VARS}"
