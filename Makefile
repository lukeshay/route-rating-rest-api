TAG=$(git rev-parse --short HEAD)

clean:
	rm -rf build/ restapi.log reports

coverage:
	./gradlew clean build -x verifyGoogleJavaFormat
	./gradlew jacocoTestCoverageVerification

format:
	./gradlew format

lint:
	./gradlew verifyGoogleJavaFormat

logs:
	docker ps | grep rest-api | awk 'NR == 1{print $$1}' | xargs docker logs

build: clean
	docker build -t rest-api . || exit 1

run: build
	docker-compose up -d || exit 1

dev: build
	docker-compose -f docker-compose.dev.yml up -d || exit 1

test:
	docker run \
      	-e JWT_SECRET=${JWT_SECRET} \
      	-e REFRESH_SECRET=${REFRESH_SECRET} \
      	-e ACCESS_KEY=${ACCESS_KEY} \
      	-e SECRET_KEY=${SECRET_KEY} \
      	--entrypoint ./scripts/test.sh \
      	rest-api:latest || exit 1

default: build
