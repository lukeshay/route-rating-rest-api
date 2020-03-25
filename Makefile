.PHONY: default clean tag build run push-latest tag-latest prebuild lambda invoke-local deploy package delete-stack

TAG=$(shell git rev-parse --short HEAD)
IMAGE_NAME=lukeshaydocker/route-rating-rest-api
SHELL = /bin/bash -o pipefail
commit_sha = $(shell git rev-parse --short HEAD)$(shell git diff --quiet || echo ".uncommitted")

zip_file = rest-api-lambda.zip
service_name = $(shell basename `git rev-parse --show-toplevel`)
build_bucket = $(service_name)-builds
lambda_entry = com.lukeshay.restapi.StreamLambdaHandler
bundle = build/distributions/$(zip_file)
latest_build_key = $(service_name)-latest/$(zip_file)
PWD = $(shell pwd)

lambda_name = $(service_name)

default: build

lambda: build/distributions/lambda
	docker-compose -f docker-compose.lambda.yml up

## build bundle
build: $(bundle)

$(bundle): build.gradle $(shell find src)
	./gradlew -x distTar -x distZip -x bootDistTar -x bootDistZip build
	touch $(bundle)

run: build/distributions/lambda
	docker run --rm -v $(PWD)/build/distributions/lambda:/var/task	\
		-e AWS_DEFAULT_REGION=$(region)								\
		-e AWS_ACCESS_KEY_ID=$(shell echo $RR_AWS_ACCESS_KEY_ID)	\
		-e AWS_SECRET_ACCESS_KEY=$(shell echo $RR_AWS_SECRET_ACCESS_KEY)\
		-e AWS_SESSION_TOKEN										\
		-e AWS_LAMBDA_FUNCTION_MEMORY_SIZE=$(memory)				\
		-e environment=dev											\
		-e version=$(version)										\
		--memory=$(memory)m											\
		lambci/lambda:java11 $(lambda_entry)

build/distributions/lambda: $(bundle)
	rm -rf build/distributions/lambda/
	unzip -o -q $(bundle) -d build/distributions/lambda/

invoke-local: $(bundle)
	sam local invoke

clean:
	rm -rf build output.yml

lambda-build:
	@docker build -t rest-api-lambda .
	@docker run -v $(PWD)/build:/app/build rest-api-lambda

lambda-package:
	@sam package \
		 --s3-bucket route-rating-rest-api-builds \
		 --s3-prefix $(TAG) \
		 --output-template-file output.yml \
		 --region us-east-2

lambda-publish:
	@sam deploy \
		   --s3-bucket route-rating-rest-api-builds \
		   --template-file output.yml \
		   --region us-east-2 \
		   --no-confirm-changeset \
		   --stack-name $(lambda_name) \
		   --capabilities CAPABILITY_IAM

lambda-vars:
	@aws lambda \
       update-function-configuration \
       --function-name "${FUNCTION_NAME}" \
       --environment "Variables={
		   GOOGLE_RECAPTCHA_TOKEN=${GOOGLE_RECAPTCHA_TOKEN},
		   REST_API_DB_PASSWORD=${REST_API_DB_PASSWORD},
		   DIGITAL_OCEAN_SECRET_KEY=${DIGITAL_OCEAN_SECRET_KEY},
		   DIGITAL_OCEAN_ACCESS_KEY=${DIGITAL_OCEAN_ACCESS_KEY},
		   REST_API_DB_URL=${REST_API_DB_URL},
		   REST_API_JWT_SECRET=${REST_API_JWT_SECRET},
		   REST_API_REFRESH_SECRET=${REST_API_REFRESH_SECRET}
		 }"

lambda-pipe: lambda-build lambda-publish lambda-vars
