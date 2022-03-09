package com.honeygo.app.hackster.config;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "neo4j.connection")
public class Neo4jConfig {

    private String uri;
    private String username;
    private String password;

    public Driver neo4jDriver(){
        return GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "test" ) );
    }
}
