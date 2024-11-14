package com.mstfcmrl.timeseries.model;

public class TimeseriesSensorData {
    private double temperature;
    private int humidity;
    private String ts;
    private double heatIndex;

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public double getHeatIndex() {
        return heatIndex;
    }

    public void setHeatIndex(double heatIndex) {
        this.heatIndex = heatIndex;
    }
}