TAG=$(shell git rev-parse --short HEAD)
IMAGE_NAME=lukeshaydocker/route-rating-rest-api

.PHONY: default clean tag build run push-latest tag-latest prebuild

default: build

clean:
	docker images | awk 'NR != 1 && $1 == "${IMAGE_NAME}" { print $3 }' | xargs docker rmi -f
	rm -rf rest-api.jar build

push:
	docker push ${IMAGE_NAME}:${TAG}

push-latest: tag-latest
	docker tag ${IMAGE_NAME}:${TAG} ${IMAGE_NAME}:latest
	docker push ${IMAGE_NAME}:latest

tag-latest:
	docker tag ${IMAGE_NAME}:${TAG} ${IMAGE_NAME}:latest

prebuild:
	sh scripts/build.sh

build:
	docker build -t ${IMAGE_NAME}:${TAG} .

run:
	docker-compose -f deploy/docker-compose.yml up -d

dev:
	docker-compose -f deploy/docker-compose.dev.yml up -d
