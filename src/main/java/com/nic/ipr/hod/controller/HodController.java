package com.nic.ipr.hod.controller;

import com.nic.ipr.hod.dto.response.HodResponse;
import com.nic.ipr.hod.service.HodService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hod")
@RequiredArgsConstructor
public class HodController {

    private final HodService hodService;

    @GetMapping("/get/all")
    public List<HodResponse> getAllHods() {
        return hodService.getAllHods();
    }
}