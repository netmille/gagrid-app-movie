FROM openjdk:8-jdk-alpine
LABEL maintainer="turik@netmille.com"
COPY target/gagrid-app-movie-0.1.1-beta.jar gagrid-app-movie-0.1.1-beta.jar
ENTRYPOINT java -jar /gagrid-app-movie-0.1.1-beta.jar --GENRE=$GENRE --spring.ds.pgsql.url=$URL --spring.ds.pgsql.username=$USERNAME --spring.ds.pgsql.password=$PASSWORD
