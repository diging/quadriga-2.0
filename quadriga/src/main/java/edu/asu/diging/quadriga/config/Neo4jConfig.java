package edu.asu.diging.quadriga.config;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.data.neo4j.core.mapping.Neo4jMappingContext;
import org.springframework.data.neo4j.core.transaction.Neo4jTransactionManager;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@Configuration
@PropertySource("classpath:config.properties")
@EnableNeo4jRepositories(basePackages = "edu.asu.diging.quadriga.core.data.neo4j")
public class Neo4jConfig {

    @Value("${neo4j.url}")
    private String neo4jUrl;

    @Value("${neo4j.username}")
    private String username;

    @Value("${neo4j.password}")
    private String password;

    @Value("${neo4j.database}")
    private String database;

    /**
     * Configures the Neo4j driver with authentication and connection details.
     */
    @Bean
    public Driver neo4jDriver() {
        return GraphDatabase.driver(neo4jUrl, AuthTokens.basic(username, password));
    }
    
    /**
     * Configures the Neo4j template, required by SDN.
     */
    @Bean
    public Neo4jTemplate neo4jTemplate(Driver driver) {
        return new Neo4jTemplate(Neo4jClient.create(driver));
    }

    /**
     * Configures the transaction manager to manage Neo4j transactions.
     */
    @Bean
    public Neo4jTransactionManager transactionManager(Driver driver) {
        return new Neo4jTransactionManager(driver);
    }
    
    @Bean
    public Neo4jMappingContext neo4jMappingContext() {
        return new Neo4jMappingContext();
    }
}