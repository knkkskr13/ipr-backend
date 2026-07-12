package com.nic.ipr.hod.dto.request;

import lombok.Data;

@Data
public class IprReturnDecisionRequest {
    // Optional note when approving; required (validated in service) when returning
    private String remarks;
}
