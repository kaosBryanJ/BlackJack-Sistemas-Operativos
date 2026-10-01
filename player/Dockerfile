FROM eclipse-temurin:23-jdk

WORKDIR /app

COPY src/main/java ./src/main/java

RUN find ./src/main/java -name "*.java" > sources.txt && \
    javac -d classes @sources.txt

CMD ["java", "-cp", "classes", "com.blackjack.player.PlayerClient"]
