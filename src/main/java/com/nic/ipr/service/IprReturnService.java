package com.nic.ipr.service;

import com.nic.ipr.dto.request.IprReturnRequest;
import com.nic.ipr.dto.request.IprReturnDecisionRequest;
import com.nic.ipr.dto.request.IprReturnUpdateRequest;
import com.nic.ipr.dto.response.IprReturnResponse;
import java.util.List;

public interface IprReturnService {
    // Employee
    IprReturnResponse addIprReturn(IprReturnRequest request);
    IprReturnResponse updateIprReturn(Long id, IprReturnUpdateRequest request);
    IprReturnResponse submitIprReturn(Long id);
    void deleteIprReturn(Long id);
    List<IprReturnResponse> getMyIprReturns();
    IprReturnResponse getIprReturnById(Long id);

    // HOD
    List<IprReturnResponse> getAllIprReturnsForHodDepartment();
    List<IprReturnResponse> getSubmittedIprReturnsForHod();
    List<IprReturnResponse> getIprReturnsByEmployeeId(Long employeeId);
    IprReturnResponse approveIprReturn(Long id, String remarks);
    IprReturnResponse returnIprReturn(Long id, String remarks);
}