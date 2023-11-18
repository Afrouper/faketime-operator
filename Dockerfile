#TODO Compile via github action
FROM gcc:13 AS BUILD_GCC

ENV VERSION=0.9.10

WORKDIR /build

RUN curl -L -O https://github.com/wolfcw/libfaketime/archive/refs/tags/v${VERSION}.tar.gz &&\
    tar -xvzf *.tar.gz &&\
    cd libfaketime-${VERSION} &&\
    PREFIX=/libfaketime/ make install

FROM quay.io/quarkus/quarkus-micro-image:2.0

COPY --from=BUILD_GCC /libfaketime /libfaketime

WORKDIR /work/
RUN chown 1001 /work \
    && chmod "g+rwX" /work \
    && chown 1001:root /work
COPY --chown=1001:root target/*-runner /work/application

EXPOSE 8080 9000
USER 1001

ENTRYPOINT ["./application", "-Dquarkus.http.host=0.0.0.0"]