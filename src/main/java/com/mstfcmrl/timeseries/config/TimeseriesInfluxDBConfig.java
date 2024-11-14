package com.mstfcmrl.timeseries.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeseriesInfluxDBConfig {

    @Value("${influxdb.url}")
    private String influxUrl;

    @Value("${influxdb.username}")
    private String influxUsername;

    @Value("${influxdb.password}")
    private String influxPassword;

    @Bean
    public InfluxDBClient influxDBClient() {
        return InfluxDBClientFactory.create(influxUrl, influxUsername, influxPassword.toCharArray());
    }
}
