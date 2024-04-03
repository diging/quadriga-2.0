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
    
    @Value("${neo4j.database}")
    private String neo4jDb;

    @Bean
    public org.neo4j.ogm.config.Configuration getConfiguration() {
        //  there might be  a  bug in Neo4j's HttpDriver? it doesn't seem to take the database.
        // for now  all data is stored in default db
        return new org.neo4j.ogm.config.Configuration.Builder().uri(neo4jUrl).database(neo4jDb).build();
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
