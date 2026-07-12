package com.nic.ipr.employee.implementation;

import com.nic.ipr.employee.dto.request.IprDeclarationRequest;
import com.nic.ipr.employee.dto.response.IprDeclarationResponse;
import com.nic.ipr.employee.entity.IprDeclaration;
import com.nic.ipr.employee.entity.IprReturn;
import com.nic.ipr.employee.entity.User;
import com.nic.ipr.employee.repository.IprDeclarationRepository;
import com.nic.ipr.employee.repository.IprReturnRepository;
import com.nic.ipr.employee.repository.UserRepository;
import com.nic.ipr.employee.service.IprDeclarationService;
import com.nic.ipr.shared.exception.BadRequestException;
import com.nic.ipr.shared.exception.ResourceNotFoundException;
import com.nic.ipr.shared.status.IprStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IprDeclarationServiceImpl implements IprDeclarationService {

    private final IprDeclarationRepository declarationRepository;
    private final IprReturnRepository iprReturnRepository;
    private final UserRepository userRepository;


    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new BadRequestException("User is not authenticated");
        }
        String username = auth.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
    private IprDeclarationResponse mapToResponse(IprDeclaration declaration) {
        IprDeclarationResponse response = new IprDeclarationResponse();
        response.setId(declaration.getId());

        if (declaration.getIprReturn() != null) {
            response.setIprReturnId(declaration.getIprReturn().getIprId());
        }

        response.setDeclarationText(declaration.getDeclarationText());
        response.setAgreed(declaration.getAgreed());
        response.setDeclarationDate(declaration.getDeclarationDate());
        response.setPlace(declaration.getPlace());
        response.setEmployeeSignature(declaration.getEmployeeSignature());
        // Map any dates/audit fields your DTO has here

        return response;
    }

    private void verifyEditable(IprReturn iprReturn, String operation) {
        if (iprReturn.getStatus() != IprStatus.DRAFT &&
                iprReturn.getStatus() != IprStatus.RETURNED) {
            throw new BadRequestException(
                    "You can only " + operation + " a declaration of a DRAFT or RETURNED IPR return");
        }
    }

    @Override
    @Transactional
    public IprDeclarationResponse addDeclaration(IprDeclarationRequest request) {
        IprReturn iprReturn = iprReturnRepository.findById(request.getIprReturnId())
                .orElseThrow(() -> new ResourceNotFoundException("IPR Return not found"));

        User user = getCurrentUser();
        if (!iprReturn.getEmployee().getId().equals(user.getEmployee().getId())) {
            throw new BadRequestException("Access denied: This IPR return does not belong to you");
        }

        verifyEditable(iprReturn, "add");

        IprDeclaration declaration = new IprDeclaration();
        declaration.setIprReturn(iprReturn);
        declaration.setDeclarationText(request.getDeclarationText());
        declaration.setAgreed(request.getAgreed());
        declaration.setDeclarationDate(request.getDeclarationDate());
        declaration.setPlace(request.getPlace());
        declaration.setEmployeeSignature(request.getEmployeeSignature());

        return mapToResponse(declarationRepository.save(declaration));
    }

    @Override
    @Transactional(readOnly = true)
    public IprDeclarationResponse getDeclarationByIprId(Long iprId) {
        return declarationRepository.findByIprReturnIprId(iprId)
                .map(this::mapToResponse)
                .orElse(null);
    }

    @Override
    @Transactional
    public IprDeclarationResponse updateDeclaration(Long id, IprDeclarationRequest request) {
        IprDeclaration existing = declarationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Declaration not found with id: " + id));

        User user = getCurrentUser();
        if (!existing.getIprReturn().getEmployee().getId().equals(user.getEmployee().getId())) {
            throw new BadRequestException("Access denied: This IPR return does not belong to you");
        }

        verifyEditable(existing.getIprReturn(), "update");

        existing.setDeclarationText(request.getDeclarationText());
        existing.setAgreed(request.getAgreed());
        existing.setDeclarationDate(request.getDeclarationDate());
        existing.setPlace(request.getPlace());
        existing.setEmployeeSignature(request.getEmployeeSignature());

        return mapToResponse(declarationRepository.save(existing));
    }

    @Override
    @Transactional
    public void deleteDeclaration(Long id) {
        IprDeclaration declaration = declarationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Declaration not found with id: " + id));

        User user = getCurrentUser();
        if (!declaration.getIprReturn().getEmployee().getId().equals(user.getEmployee().getId())) {
            throw new BadRequestException("Access denied: This IPR return does not belong to you");
        }

        verifyEditable(declaration.getIprReturn(), "delete");

        declarationRepository.deleteById(id);
    }

}