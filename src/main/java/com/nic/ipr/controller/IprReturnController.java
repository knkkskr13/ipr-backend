package com.nic.ipr.controller;

import com.nic.ipr.dto.request.IprReturnRequest;
import com.nic.ipr.dto.request.IprReturnUpdateRequest;
import com.nic.ipr.dto.response.IprReturnResponse;
import com.nic.ipr.service.IprReturnService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ipr-return")
@RequiredArgsConstructor
public class IprReturnController {

    private final IprReturnService iprReturnService;

    @GetMapping("/get")
    public List<IprReturnResponse> getAllIprReturns() {
        return iprReturnService.getAllIprReturns();
    }

    @GetMapping("/get/{id}")
    public IprReturnResponse getIprReturnById(@PathVariable Long id) {
        return iprReturnService.getIprReturnById(id);
    }

    @GetMapping("/get/employee/{employeeId}")
    public List<IprReturnResponse> getIprReturnsByEmployeeId(@PathVariable Long employeeId) {
        return iprReturnService.getIprReturnsByEmployeeId(employeeId);
    }

    @PostMapping("/add")
    public IprReturnResponse addIprReturn(@Valid @RequestBody IprReturnRequest request) {
        return iprReturnService.addIprReturn(request);
    }

    @PutMapping("/update/{id}")
    public IprReturnResponse updateIprReturn(@PathVariable Long id,
                                             @Valid @RequestBody IprReturnUpdateRequest request) {
        return iprReturnService.updateIprReturn(id, request);
    }

    @PutMapping("/update/{id}/submit")
    public IprReturnResponse submitIprReturn(@PathVariable Long id) {
        return iprReturnService.submitIprReturn(id);
    }

    @PutMapping("/update/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public IprReturnResponse approveIprReturn(@PathVariable Long id) {
        return iprReturnService.approveIprReturn(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteIprReturn(@PathVariable Long id) {
        iprReturnService.deleteIprReturn(id);
    }
}