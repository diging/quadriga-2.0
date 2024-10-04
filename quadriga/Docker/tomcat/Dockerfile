#base image
FROM tomcat:11.0.0-jdk17

ENV JAVA_OPTS="-DappConfigFile=file:/usr/local/tomcat/data/config"
ENV TOMCAT_VERSION 11.0.0-M6

RUN mkdir app
RUN mkdir data
RUN mkdir data/config
RUN mkdir data/files

# Install dependencies
RUN apt-get update && apt-get install git -y && apt install maven -y

# Download and extract Tomcat
RUN wget -O /tmp/tomcat.tar.gz https://dlcdn.apache.org/tomcat/tomcat-11/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz && \
    tar xf /tmp/tomcat.tar.gz -C /opt && \
    rm /tmp/tomcat.tar.gz && \
    mv /opt/apache-tomcat-${TOMCAT_VERSION} /opt/tomcat

# Set environment variables
ENV CATALINA_HOME /usr/local/tomcat
ENV PATH $CATALINA_HOME/bin:$PATH

# Expose Tomcat port
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]