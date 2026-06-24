package com.nic.ipr.service;

import com.nic.ipr.entity.IprDeclaration;
import com.nic.ipr.repository.IprDeclarationRepository;
import com.nic.ipr.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IprDeclarationService {

    private final IprDeclarationRepository declarationRepository;

    public IprDeclaration getDeclarationByIprId(Long iprId) {
        return declarationRepository.findByIprReturnIprId(iprId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Declaration not found for IPR id: " + iprId));
    }

    public IprDeclaration addDeclaration(IprDeclaration declaration) {
        return declarationRepository.save(declaration);
    }

    public IprDeclaration updateDeclaration(Long id,
                                            IprDeclaration updatedDeclaration) {

        IprDeclaration existingDeclaration =
                declarationRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Declaration not found with id: " + id));

        existingDeclaration.setDeclarationText(
                updatedDeclaration.getDeclarationText());
        existingDeclaration.setAgreed(
                updatedDeclaration.getAgreed());
        existingDeclaration.setDeclarationDate(
                updatedDeclaration.getDeclarationDate());
        existingDeclaration.setPlace(
                updatedDeclaration.getPlace());
        existingDeclaration.setEmployeeSignature(
                updatedDeclaration.getEmployeeSignature());

        return declarationRepository.save(existingDeclaration);
    }

    public void deleteDeclaration(Long id) {

        IprDeclaration declaration =
                declarationRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Declaration not found with id: " + id));

        declarationRepository.delete(declaration);
    }
}