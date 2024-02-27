FROM maven:3-openjdk-17-slim AS builder

WORKDIR /opt

COPY . .

RUN mvn clean package -DskipTests=true -Dmaven.javadoc.skip=true -B -V

FROM openjdk:17-slim AS runner

WORKDIR /opt

COPY --from=builder target/*.jar api.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "/opt/api.jar"]
