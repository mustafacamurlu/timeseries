package com.mstfcmrl.timeseries.controller;

import com.mstfcmrl.timeseries.service.TimeseriesService;
import com.mstfcmrl.timeseries.model.TimeseriesDataPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/timeseries")
public class TimeseriesController {

    @Autowired
    private TimeseriesService timeSeriesService;

    @GetMapping("/test")
    public String publicHello() {
        String hostname = System.getenv("HOSTNAME");
        return "Hello, this is a public endpoint!" + hostname;
    }

    @GetMapping("/write")
    public String writeData() {
        timeSeriesService.writeData();
        return "Data written successfully!";
    }

    @GetMapping("/query")
    public String queryData() {
        return timeSeriesService.queryData();
    }

    @GetMapping("/sleep")
    public String sleepEndpoint() {
        String hostname = System.getenv("HOSTNAME");
        try {
            // Sleep for 3 seconds
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Error: Interrupted while sleeping";
        }
        System.out.println("Slept for 3 seconds" + hostname);
        return "Slept for 3 seconds" + hostname;
    }

    @PostMapping
    public ResponseEntity<String> writeIoTData(@Valid @RequestBody TimeseriesDataPayload payload) {
        try {
            // Call service to write data to InfluxDB
            timeSeriesService.writeSensorData(payload.getDeviceId(), payload.getValues());

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to write data: " + e.getMessage());
        }
    }
}
