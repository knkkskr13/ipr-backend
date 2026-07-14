package com.nic.ipr.implementation;

import com.nic.ipr.dto.request.IprDeclarationRequest;
import com.nic.ipr.dto.response.IprDeclarationResponse;
import com.nic.ipr.entity.IprDeclaration;
import com.nic.ipr.entity.IprReturn;
import com.nic.ipr.repository.IprDeclarationRepository;
import com.nic.ipr.repository.IprReturnRepository;
import com.nic.ipr.service.IprDeclarationService;
import com.nic.ipr.shared.enums.IprStatus;
import com.nic.ipr.shared.exception.BadRequestException;
import com.nic.ipr.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IprDeclarationServiceImpl implements IprDeclarationService {

    private final IprDeclarationRepository declarationRepository;
    private final IprReturnRepository iprReturnRepository;

    @Override
    public IprDeclarationResponse addDeclaration(IprDeclarationRequest request) {
        IprReturn iprReturn = iprReturnRepository.findById(request.getIprReturnId())
                .orElseThrow(() -> new ResourceNotFoundException("IPR Return not found"));

        if (iprReturn.getStatus() != IprStatus.DRAFT && iprReturn.getStatus() != IprStatus.RETURNED) {
            throw new BadRequestException("Cannot add declaration to a submitted return");
        }

        if (declarationRepository.existsByIprReturnIprId(request.getIprReturnId())) {
            throw new BadRequestException("Declaration already exists for this IPR return");
        }

        IprDeclaration declaration = new IprDeclaration();
        declaration.setIprReturn(iprReturn);
        declaration.setDeclarationText(request.getDeclarationText());
        declaration.setAgreed(request.getAgreed());
        declaration.setDeclarationDate(request.getDeclarationDate());
        declaration.setPlace(request.getPlace());
        declaration.setEmployeeSignature(request.getEmployeeSignature());

        return toResponse(declarationRepository.save(declaration));
    }

    @Override
    public IprDeclarationResponse updateDeclaration(Long id, IprDeclarationRequest request) {
        IprDeclaration declaration = declarationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Declaration not found"));

        if (declaration.getIprReturn().getStatus() != IprStatus.DRAFT &&
                declaration.getIprReturn().getStatus() != IprStatus.RETURNED) {
            throw new BadRequestException("Cannot update declaration on a submitted return");
        }

        declaration.setDeclarationText(request.getDeclarationText());
        declaration.setAgreed(request.getAgreed());
        declaration.setDeclarationDate(request.getDeclarationDate());
        declaration.setPlace(request.getPlace());
        declaration.setEmployeeSignature(request.getEmployeeSignature());

        return toResponse(declarationRepository.save(declaration));
    }

    @Override
    public void deleteDeclaration(Long id) {
        IprDeclaration declaration = declarationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Declaration not found"));

        if (declaration.getIprReturn().getStatus() != IprStatus.DRAFT &&
                declaration.getIprReturn().getStatus() != IprStatus.RETURNED) {
            throw new BadRequestException("Cannot delete declaration on a submitted return");
        }

        if (declaration.getIprReturn() != null) {
            declaration.getIprReturn().setDeclaration(null);
            declaration.setIprReturn(null);
        }

        declarationRepository.delete(declaration);
    }

    @Override
    public IprDeclarationResponse getDeclarationByIprId(Long iprId) {
        return toResponse(declarationRepository.findByIprReturnIprId(iprId)
                .orElseThrow(() -> new ResourceNotFoundException("Declaration not found")));
    }

    private IprDeclarationResponse toResponse(IprDeclaration d) {
        IprDeclarationResponse response = new IprDeclarationResponse();
        response.setId(d.getId());
        response.setIprReturnId(d.getIprReturn().getIprId());
        response.setDeclarationText(d.getDeclarationText());
        response.setAgreed(d.getAgreed());
        response.setDeclarationDate(d.getDeclarationDate());
        response.setPlace(d.getPlace());
        response.setEmployeeSignature(d.getEmployeeSignature());
        return response;
    }
}