TAG=$(shell git rev-parse --short HEAD)
IMAGE_NAME=lukeshaydocker/route-rating-rest-api

.PHONY: default clean tag build run push-latest tag-latest prebuild

default: build

clean:
	docker images | awk 'NR != 1 && $1 == "${IMAGE_NAME}" { print $3 }' | xargs docker rmi -f

push:
	docker push ${IMAGE_NAME}:${TAG}

push-latest: tag-latest
	docker tag ${IMAGE_NAME}:${TAG} ${IMAGE_NAME}:latest
	docker push ${IMAGE_NAME}:latest

tag-latest:
	docker tag ${IMAGE_NAME}:${TAG} ${IMAGE_NAME}:latest

prebuild:
	sh scripts/build.sh

build: prebuild
	docker build -t ${IMAGE_NAME}:${TAG} .

run:
	docker-compose -f deploy/docker-compose.yml up -d

dev:
	docker-compose -f deploy/docker-compose.dev.yml up -d

#test:
#	docker run \
#      	-e JWT_SECRET=${JWT_SECRET} \
#      	-e REFRESH_SECRET=${REFRESH_SECRET} \
#      	-e ACCESS_KEY=${ACCESS_KEY} \
#      	-e SECRET_KEY=${SECRET_KEY} \
#      	--entrypoint ./scripts/test.sh \
#      	rest-api:${TAG} || exit 1
