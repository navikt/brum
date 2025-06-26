FROM gcr.io/distroless/java21-debian12

COPY build/libs/*.jar /app/
COPY files/test-data.csv files/test-data.csv

WORKDIR /app

CMD ["app.jar"]