FROM gcr.io/distroless/java21-debian12

COPY build/libs/*.jar /app/
COPY files/ /files/
WORKDIR /app

CMD ["app.jar"]