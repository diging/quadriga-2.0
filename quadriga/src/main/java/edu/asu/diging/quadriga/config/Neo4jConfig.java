package edu.asu.diging.quadriga.config;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;

@Configuration
@PropertySource("classpath:config.properties")
@EnableNeo4jRepositories(basePackages = "edu.asu.diging.quadriga.core.data.neo4j")
public class Neo4jConfig {

    @Value("${neo4j.url}")
    private String neo4jUrl;

    @Bean
    public org.neo4j.ogm.config.Configuration getConfiguration() {
        return new org.neo4j.ogm.config.Configuration.Builder().uri(neo4jUrl).build();
    }

    @Bean
    public SessionFactory sessionFactory() {
        return new SessionFactory(getConfiguration(), "edu.asu.diging.quadriga.core.model.mapped");
    }

    @Bean
    public Neo4jTransactionManager transactionManager() {
        return new Neo4jTransactionManager(sessionFactory());
    }
}
