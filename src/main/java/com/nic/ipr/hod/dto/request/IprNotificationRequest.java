package com.nic.ipr.hod.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class IprNotificationRequest {

    @NotBlank(message = "Session is required")
    private String session;

    @NotBlank(message = "Message is required")
    private String message;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date cannot be in the past")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @FutureOrPresent(message = "End date cannot be in the past")
    private LocalDate endDate;

    private Boolean active = true;

    @NotNull(message = "Office ID is required")
    private Long officeId;
}