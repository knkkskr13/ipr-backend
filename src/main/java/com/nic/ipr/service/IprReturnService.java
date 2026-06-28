package com.nic.ipr.service;

import com.nic.ipr.dto.request.IprReturnRequest;
import com.nic.ipr.dto.request.IprReturnUpdateRequest;
import com.nic.ipr.dto.response.IprReturnResponse;

import java.util.List;

public interface IprReturnService {

    List<IprReturnResponse> getAllIprReturns();

    IprReturnResponse getIprReturnById(Long id);

    List<IprReturnResponse> getIprReturnsByEmployeeId(Long employeeId);

    IprReturnResponse addIprReturn(IprReturnRequest request);

    IprReturnResponse updateIprReturn(Long id, IprReturnUpdateRequest request);

    IprReturnResponse submitIprReturn(Long id);

    IprReturnResponse approveIprReturn(Long id);

    void deleteIprReturn(Long id);
}