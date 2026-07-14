package com.nic.ipr.employee.service;

import com.nic.ipr.employee.dto.request.IprDeclarationRequest;
import com.nic.ipr.employee.dto.response.IprDeclarationResponse;

public interface IprDeclarationService {
    IprDeclarationResponse addDeclaration(IprDeclarationRequest request);
    IprDeclarationResponse getDeclarationByIprId(Long iprId);
    IprDeclarationResponse updateDeclaration(Long id, IprDeclarationRequest request);
    void deleteDeclaration(Long id);
}