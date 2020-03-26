FROM adoptopenjdk/openjdk11:latest

RUN mkdir -p /app
ENV PROJECT_HOME /app

WORKDIR $PROJECT_HOME

EXPOSE 8080

COPY ./rest-api.jar $PROJECT_HOME

ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom \
  -Dspring.datasource.url=$DB_URL \
  -Dspring.datasource.username=$DB_USERNAME \
  -Dspring.datasource.password=$DB_PASSWORD \
  -Dsecurity.token.jwt=$JWT_SECRET \
  -Dsecurity.token.refresh=$REFRESH_SECRET \
  -Ddo.key.access=$ACCESS_KEY \
  -Ddo.key.secret=$SECRET_KEY \
  -Dgoogle.recaptcha.token=$GOOGLE_RECAPTCHA_TOKEN \
  -jar rest-api.jar || exit 1
