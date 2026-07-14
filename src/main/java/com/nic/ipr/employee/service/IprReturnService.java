package com.nic.ipr.employee.service;

import com.nic.ipr.employee.dto.request.IprReturnRequest;
import com.nic.ipr.employee.dto.request.IprReturnUpdateRequest;
import com.nic.ipr.employee.dto.response.IprReturnResponse;

import java.util.List;

public interface IprReturnService {
    // Employee-side operations (act on the current employee's own returns)
    List<IprReturnResponse> getAllIprReturns();
    IprReturnResponse getIprReturnById(Long id);
    List<IprReturnResponse> getIprReturnsByEmployeeId(Long employeeId);
    IprReturnResponse addIprReturn(IprReturnRequest request);
    IprReturnResponse updateIprReturn(Long id, IprReturnUpdateRequest request);
    IprReturnResponse submitIprReturn(Long id);
    void deleteIprReturn(Long id);

    // HOD-side operations (act on returns filed by employees in the HOD's department)
    List<IprReturnResponse> getSubmittedIprReturnsForHod();
    List<IprReturnResponse> getAllIprReturnsForHodDepartment();

    IprReturnResponse approveIprReturn(Long id, String remarks);
    IprReturnResponse returnIprReturn(Long id, String remarks);
}