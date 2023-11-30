package ru.practicum.explore_with_me.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import statclient.StatisticsClient;

@Configuration
public class StatisticsClientConfiguration {

    @Value("${stats-service.url}")
    private String url;

    @Bean
    StatisticsClient statsClient() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        return new StatisticsClient(url, builder);
    }
}
