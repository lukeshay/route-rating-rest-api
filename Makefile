TAG=$(shell git rev-parse --short HEAD)

default: build

clean:
	rm -rf build/ restapi.log reports

tag:
	docker tag rest-api:${TAG} rest-api:latest

coverage:
	./gradlew clean build -x verifyGoogleJavaFormat
	./gradlew jacocoTestCoverageVerification

format:
	./gradlew format

lint:
	./gradlew verifyGoogleJavaFormat

logs:
	docker ps | grep rest-api | awk 'NR == 1{print $$1}' | xargs docker logs

build:
	docker build -t rest-api:${TAG} . || exit 1

run:
	docker-compose up -d || exit 1

dev:
	docker-compose -f docker-compose.dev.yml up -d || exit 1

test:
	docker run \
      	-e JWT_SECRET=${JWT_SECRET} \
      	-e REFRESH_SECRET=${REFRESH_SECRET} \
      	-e ACCESS_KEY=${ACCESS_KEY} \
      	-e SECRET_KEY=${SECRET_KEY} \
      	--entrypoint ./scripts/test.sh \
      	rest-api:${TAG} || exit 1
