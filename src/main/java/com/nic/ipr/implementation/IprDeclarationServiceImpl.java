package com.nic.ipr.implementation;

import com.nic.ipr.dto.request.IprDeclarationRequest;
import com.nic.ipr.dto.response.IprDeclarationResponse;
import com.nic.ipr.entity.IprDeclaration;
import com.nic.ipr.entity.IprReturn;
import com.nic.ipr.entity.User;
import com.nic.ipr.exception.BadRequestException;
import com.nic.ipr.exception.ResourceNotFoundException;
import com.nic.ipr.repository.IprDeclarationRepository;
import com.nic.ipr.repository.IprReturnRepository;
import com.nic.ipr.repository.UserRepository;
import com.nic.ipr.service.IprDeclarationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IprDeclarationServiceImpl implements IprDeclarationService {

    private final IprDeclarationRepository declarationRepository;
    private final IprReturnRepository iprReturnRepository;
    private final UserRepository userRepository;

    // ----------------------------------------------------------------
    // HELPER METHODS
    // ----------------------------------------------------------------

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }

    private void verifyOwnership(IprReturn iprReturn) {
        User user = getCurrentUser();
        if (user.getRole().equalsIgnoreCase("EMPLOYEE")
                && !iprReturn.getEmployee().getId()
                .equals(user.getEmployee().getId())) {
            throw new BadRequestException(
                    "Access denied: You can only access your own declarations");
        }
    }

    // ----------------------------------------------------------------
    // MAPPER METHODS
    // ----------------------------------------------------------------

    private IprDeclarationResponse mapToResponse(IprDeclaration declaration) {
        IprDeclarationResponse response = new IprDeclarationResponse();
        response.setDeclarationId(declaration.getDeclarationId());
        response.setIprId(declaration.getIprReturn().getIprId()); // just ID, no nesting!
        response.setDeclarationText(declaration.getDeclarationText());
        response.setAgreed(declaration.getAgreed());
        response.setDeclarationDate(declaration.getDeclarationDate());
        response.setPlace(declaration.getPlace());
        response.setEmployeeSignature(declaration.getEmployeeSignature());
        response.setCreatedAt(declaration.getCreatedAt());
        response.setUpdatedAt(declaration.getUpdatedAt());
        return response;
    }

    // ----------------------------------------------------------------
    // SERVICE METHODS
    // ----------------------------------------------------------------

    @Override
    public IprDeclarationResponse getDeclarationByIprId(Long iprId) {

        IprReturn iprReturn = iprReturnRepository.findById(iprId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "IPR Return not found with id: " + iprId));

        verifyOwnership(iprReturn);

        IprDeclaration declaration = declarationRepository
                .findByIprReturnIprId(iprId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Declaration not found for IPR id: " + iprId));

        return mapToResponse(declaration);
    }

    @Override
    public IprDeclarationResponse addDeclaration(IprDeclarationRequest request) {

        // Find the IprReturn this declaration belongs to
        IprReturn iprReturn = iprReturnRepository.findById(request.getIprId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "IPR Return not found with id: " + request.getIprId()));

        // Verify ownership
        verifyOwnership(iprReturn);

        // Map request to entity
        IprDeclaration declaration = new IprDeclaration();
        declaration.setIprReturn(iprReturn);
        declaration.setDeclarationText(request.getDeclarationText());
        declaration.setAgreed(request.getAgreed());
        declaration.setDeclarationDate(request.getDeclarationDate());
        declaration.setPlace(request.getPlace());
        declaration.setEmployeeSignature(request.getEmployeeSignature());

        IprDeclaration saved = declarationRepository.save(declaration);
        return mapToResponse(saved);
    }

    @Override
    public IprDeclarationResponse updateDeclaration(Long id,
                                                    IprDeclarationRequest request) {

        IprDeclaration existing = declarationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Declaration not found with id: " + id));

        // Verify ownership using existing declaration's IprReturn
        verifyOwnership(existing.getIprReturn());

        existing.setDeclarationText(request.getDeclarationText());
        existing.setAgreed(request.getAgreed());
        existing.setDeclarationDate(request.getDeclarationDate());
        existing.setPlace(request.getPlace());
        existing.setEmployeeSignature(request.getEmployeeSignature());

        IprDeclaration saved = declarationRepository.save(existing);
        return mapToResponse(saved);
    }

    @Override
    public void deleteDeclaration(Long id) {

        IprDeclaration declaration = declarationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Declaration not found with id: " + id));

        verifyOwnership(declaration.getIprReturn());
        declarationRepository.delete(declaration);
    }
}