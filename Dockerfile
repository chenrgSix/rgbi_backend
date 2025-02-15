FROM openjdk:8
VOLUME /tmp
COPY target/*.jar intelligent-0.0.1-SNAPSHOT.jar
ENV PORT=8101
ENTRYPOINT ["java","-jar","-Xms64m","-Xmx128m","intelligent-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]
EXPOSE $PORT