FROM adoptopenjdk/openjdk11:latest

RUN mkdir -p /app
ENV PROJECT_HOME /app

WORKDIR $PROJECT_HOME

COPY ./rest-api.jar $PROJECT_HOME

EXPOSE 8080

ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom \
  -Dspring.datasource.url=jdbc:postgresql://rest-api-postgres:5432/routerating \
  -DJWT_SECRET=$JWT_SECRET \
  -DREFRESH_SECRET=$REFRESH_SECRET \
  -DACCESS_KEY=$ACCESS_KEY \
  -DSECRET_KEY=$SECRET_KEY \
  -DGOOGLE_RECAPTCHA_TOKEN=$GOOGLE_RECAPTCHA_TOKEN \
  -jar rest-api.jar || exit 1
