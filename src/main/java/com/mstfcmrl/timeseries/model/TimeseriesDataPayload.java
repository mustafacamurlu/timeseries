package com.mstfcmrl.timeseries.model;

import com.mstfcmrl.timeseries.model.TimeseriesSensorData;

import java.util.List;

public class TimeseriesDataPayload {

    private String deviceId;
    private List<TimeseriesSensorData> values;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public List<TimeseriesSensorData> getValues() {
        return values;
    }

    public void setValues(List<TimeseriesSensorData> values) {
        this.values = values;
    }
}

