package com.nic.ipr.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class IprNotificationRequest {

    @NotBlank(message = "Title is required")
    private String title;               // String Not blank

    @NotBlank(message = "Message is required")
    private String message;                 // String Not blank

    @NotNull(message = "Start date is required")
    private LocalDate startDate;            // calender Date Not blank

    @NotNull(message = "End date is required")
    private LocalDate endDate;              // calender Date Not blank

    private Boolean active = true;          // defaults to active when created
}