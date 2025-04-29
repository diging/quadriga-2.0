FROM tomcat:9.0.95-jdk17

ENV JAVA_OPTS="-DappConfigFile=file:/usr/local/tomcat/data/config"

RUN mkdir app
RUN mkdir data
RUN mkdir data/config
RUN mkdir data/files

WORKDIR .

RUN apt-get update && apt-get install git -y && apt install maven -y

#RUN cd quadriga-2.0/quadriga && mvn clean package -Dlog.level=info
RUN cp quadriga-2.0/quadriga/target/quadriga.war ../webapps

RUN cp ./scripts/start.sh .
RUN ["chmod", "+x", "start.sh"]

# make sure to wait for mysql
ENV DOCKERIZE_VERSION v0.6.1
RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz

CMD dockerize -wait tcp://db:3306 -timeout 60m ./start.sh