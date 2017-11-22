FROM java:8-alpine

EXPOSE 8080

ADD build/libs/spring.jar /usr/local/lib/spring.jar

CMD exec /usr/bin/java -jar /usr/local/lib/spring.jar