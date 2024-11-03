ARG ENVIRONMENT=production

FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY . .

RUN if [ "$ENVIRONMENT" = "production" ]; then \
      chmod +x ./mvnw && ./mvnw clean package -DskipTests; \
    else \
      echo "Ambiente de desenvolvimento detectado, pulando build"; \
    fi

FROM openjdk:17-jdk-slim
WORKDIR /app

ARG ENVIRONMENT
COPY --from=build /app/target/zenflow-back-end-java-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
CMD if [ "$ENVIRONMENT" = "production" ]; then \
      java -jar app.jar; \
    else \
      echo "Ambiente de desenvolvimento, rodando servidor local"; \
    fi
