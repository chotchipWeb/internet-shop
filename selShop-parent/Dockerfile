FROM bellsoft/liberica-openjdk-alpine:17 as build

ARG JAR_FILE
WORKDIR /build

ADD $JAR_FILE application.jar
RUN java -Djarmode=layertools -jar application.jar extract --destination extracted

FROM bellsoft/liberica-openjdk-alpine:17

RUN adduser --system spring-boot && addgroup --system spring-boot-group && adduser spring-boot spring-boot-group
USER spring-boot:spring-boot-group
VOLUME /tmp
WORKDIR /application

COPY --from=build /build/extracted/dependencies .
COPY --from=build /build/extracted/spring-boot-loader .
COPY --from=build /build/extracted/snapshot-dependencies .
COPY --from=build /build/extracted/application .

ENTRYPOINT exec java ${JAVA_OPTS} org.springframework.boot.loader.launch.JarLauncher ${0} ${@}