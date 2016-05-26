package com.bsu.famcs.wscan.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.zaproxy.clientapi.core.ClientApi;

@Configuration
@ComponentScan(basePackages = "com.bsu.famcs.wscan")
@PropertySource("classpath:globals.properties")
public class ApplConfiguration {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public static ClientApi clientApi(@Value("${API_HOST}") String host,
                                      @Value("${API_PORT}") int port){
        return new ClientApi(host,port);
    }
}
