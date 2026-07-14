package com.nic.ipr.service;

import com.nic.ipr.dto.request.IprDeclarationRequest;
import com.nic.ipr.dto.response.IprDeclarationResponse;

public interface IprDeclarationService {
    IprDeclarationResponse addDeclaration(IprDeclarationRequest request);
    IprDeclarationResponse updateDeclaration(Long id, IprDeclarationRequest request);
    void deleteDeclaration(Long id);
    IprDeclarationResponse getDeclarationByIprId(Long iprId);
}