FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

RUN printf '%s\n' \
'<settings><mirrors><mirror>' \
'<id>google-central</id><mirrorOf>central</mirrorOf>' \
'<url>https://maven-central.storage-download.googleapis.com/maven2/</url>' \
'</mirror></mirrors></settings>' > /tmp/settings.xml

COPY pom.xml .
RUN mvn -s /tmp/settings.xml dependency:go-offline -B

COPY src ./src
RUN mvn -s /tmp/settings.xml clean package -DskipTests -B

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]