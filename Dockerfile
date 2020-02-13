FROM adoptopenjdk:8-jdk-hotspot

RUN mkdir -p /opt/app
ENV PROJECT_HOME /opt/app

WORKDIR $PROJECT_HOME

COPY . $PROJECT_HOME

EXPOSE 8080

RUN chmod 755 scripts/*.sh

RUN sh ./scripts/build.sh

ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -Dspring.datasource.url=jdbc:postgresql://rest-api-postgres:5432/routerating -DJWT_SECRET=$JWT_SECRET -DREFRESH_SECRET=$REFRESH_SECRET -DACCESS_KEY=$ACCESS_KEY -DSECRET_KEY=$SECRET_KEY -jar rest-api.jar || exit 1
