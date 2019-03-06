FROM tomcat:8.0-jre8

ENV WEBAPP_PATH /usr/local/tomcat/webapps/ROOT
ENV TMP_CONF_IN_FOLDER /tmp/conf/
ENV TMP_CONF_OUT_FOLDER ${WEBAPP_PATH}/WEB-INF/classes
ENV TIMEZONE Europe/Paris

ARG VERSION

COPY ./docker/tomcat/conf/* $TMP_CONF_IN_FOLDER
COPY ./docker/tomcat/bin/startup /usr/bin/startup
RUN chmod 744 /usr/bin/startup

RUN rm -rf ${WEBAPP_PATH}
RUN mkdir ${WEBAPP_PATH}

RUN unlink /etc/localtime
RUN ln -s /usr/share/zoneinfo/${TIMEZONE} /etc/localtime
RUN echo "${TIMEZONE}" > /etc/timezone

COPY ./target/Project/ $WEBAPP_PATH

CMD startup
