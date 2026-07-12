package com.nic.ipr.hod.service;

import com.nic.ipr.hod.dto.response.HodResponse;

import java.util.List;

public interface HodService {
    List<HodResponse> getAllHods();
}