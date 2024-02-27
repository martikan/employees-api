package com.martikan.employeeapi.dto;


import lombok.Getter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class ApiResponse<T> {

    private final String status;

    private final T message;

    private final String time;

    public ApiResponse(final String status, final T message) {
        this.status = status;
        this.message = message;
        this.time = ZonedDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
