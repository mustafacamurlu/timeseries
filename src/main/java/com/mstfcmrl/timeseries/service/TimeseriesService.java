package com.mstfcmrl.timeseries.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mstfcmrl.timeseries.model.TimeseriesSensorData;
import java.util.List;
import java.util.stream.Collectors;

import java.time.Instant;

@Service
public class TimeseriesService {

    @Autowired
    private InfluxDBClient influxDBClient;

    private final String org = "optimove_org";
    private final String bucket = "optimove_bucket";

    public void writeData() {
        Point point = Point.measurement("temperature")
                .addTag("location", "room1")
                .addField("value", 23.5)
                .time(Instant.now(), WritePrecision.S);

        influxDBClient.getWriteApiBlocking().writePoint(bucket, org, point);
    }

    public String queryData() {
        String flux = "from(bucket: \"" + bucket + "\") |> range(start: -1h)";
        return influxDBClient.getQueryApi().queryRaw(flux, org);
    }

    public void writeSensorData(String deviceId, List<TimeseriesSensorData> values) {

        List<TimeseriesSensorData> modifiedValues = modifySensorData(values);

        List<Point> points = modifiedValues.stream().map(valueWithTimestamp ->
                Point.measurement("iot_device_data")
                        .addTag("device_id", deviceId)
                        .addField("temperature", valueWithTimestamp.getTemperature())
                        .addField("humidity", valueWithTimestamp.getHumidity())
                        .addField("heat_index", valueWithTimestamp.getHeatIndex())
                        .time(Instant.parse(valueWithTimestamp.getTs()), WritePrecision.NS)
        ).collect(Collectors.toList());

        influxDBClient.getWriteApiBlocking().writePoints(bucket, org, points);

    }

    private List<TimeseriesSensorData> modifySensorData(List<TimeseriesSensorData> values) {
        return values.stream().map(value -> {
            double temperatureInFahrenheit = (value.getTemperature() * 9/5) + 32;
            int reducedHumidity = value.getHumidity() - 10;
            double heatIndex = calculateHeatIndex(temperatureInFahrenheit, reducedHumidity);

            TimeseriesSensorData modifiedValue = new TimeseriesSensorData();
            modifiedValue.setTemperature(temperatureInFahrenheit);
            modifiedValue.setHumidity(reducedHumidity);
            modifiedValue.setHeatIndex(heatIndex);
            modifiedValue.setTs(value.getTs());

            return modifiedValue;
        }).collect(Collectors.toList());
    }

    private double calculateHeatIndex(double temperature, int humidity) {
        return -42.379 + 2.04901523 * temperature + 10.14333127 * humidity
                - 0.22475541 * temperature * humidity - 0.00683783 * temperature * temperature
                - 0.05481717 * humidity * humidity + 0.00122874 * temperature * temperature * humidity
                + 0.00085282 * temperature * humidity * humidity
                - 0.00000199 * temperature * temperature * humidity * humidity;
    }
}
