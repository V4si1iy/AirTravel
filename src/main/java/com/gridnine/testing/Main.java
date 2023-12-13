package com.gridnine.testing;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Handler handler = new Handler();
        handler.handle(FlightBuilder.createFlights());
    }
}
