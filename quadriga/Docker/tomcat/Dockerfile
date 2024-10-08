FROM tomcat:9.0.95-jdk17


ENV JAVA_OPTS="-DappConfigFile=file:/jdk17-path/bin"
ENV TOMCAT_VERSION 9.0.95
ENV CATALINA_HOME /usr/local/tomcat

RUN mkdir app
RUN mkdir data
RUN mkdir data/config
RUN mkdir data/files

WORKDIR app

RUN apt clean && \
    rm -rf /var/lib/apt/lists/*

RUN apt-get update && apt-get install git -y && apt install maven -y

RUN git clone -b develop https://github.com/diging/quadriga-2.0.git

RUN cd quadriga-2.0/quadriga && mvn clean package -Dlog.level=info
RUN cp quadriga-2.0/quadriga/target/vspace.war ../webapps

# download tomcat manager app
# RUN wget https://mirrors.ocf.berkeley.edu/apache/tomcat/tomcat-$TOMCAT_VERSION/v$TOMCAT_VERSION/bin/apache-tomcat-$TOMCAT_VERSION.zip
# RUN unzip apache-tomcat-$TOMCAT_VERSION.zip
RUN wget https://downloads.apache.org/tomcat/tomcat-11/v$TOMCAT_VERSION/bin/apache-tomcat-$TOMCAT_VERSION.tar.gz \
    && tar -xvzf apache-tomcat-$TOMCAT_VERSION.tar.gz \
    && mv apache-tomcat-$TOMCAT_VERSION $CATALINA_HOME \
    && rm apache-tomcat-$TOMCAT_VERSION.tar.gz
RUN cp -r apache-tomcat-$TOMCAT_VERSION/webapps/manager ../webapps

# COPY scripts/tomcat-users.xml ../conf/
# COPY scripts/context.xml ../webapps/manager/META-INF/
# COPY scripts/start-tomcat-manager.sh .
RUN ["chmod", "+x", "/usr/local/tomcat/bin/catalina.sh run"]

# make sure to wait for mysql
ENV DOCKERIZE_VERSION v0.6.1
RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz

CMD dockerize -wait tcp://db:3306 -timeout 60m ./start-tomcat-manager.sh