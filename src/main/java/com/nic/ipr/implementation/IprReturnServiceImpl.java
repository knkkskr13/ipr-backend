package com.nic.ipr.implementation;

import com.nic.ipr.dto.request.IprReturnRequest;
import com.nic.ipr.dto.request.IprReturnUpdateRequest;
import com.nic.ipr.dto.response.IprReturnResponse;
import com.nic.ipr.entity.Employee;
import com.nic.ipr.entity.IprReturn;
import com.nic.ipr.entity.User;
import com.nic.ipr.exception.BadRequestException;
import com.nic.ipr.exception.ResourceNotFoundException;
import com.nic.ipr.repository.IprReturnRepository;
import com.nic.ipr.repository.UserRepository;
import com.nic.ipr.service.IprNotificationService;
import com.nic.ipr.service.IprReturnService;
import com.nic.ipr.status.IprStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IprReturnServiceImpl implements IprReturnService {

    private final IprReturnRepository iprReturnRepository;
    private final UserRepository userRepository;
    private final IprNotificationService iprNotificationService;

    // ----------------------------------------------------------------
    // HELPER METHODS
    // ----------------------------------------------------------------

    // Get currently logged in user from JWT
    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }

    // Check if logged in employee owns this IprReturn
    private void verifyOwnership(IprReturn iprReturn) {
        User user = getCurrentUser();
        if (user.getRole().equalsIgnoreCase("EMPLOYEE")
                && !iprReturn.getEmployee().getId()
                .equals(user.getEmployee().getId())) {
            throw new BadRequestException(
                    "Access denied: You can only access your own IPR returns");
        }
    }

    // Check if logged in employee matches the requested employeeId
    private void verifyEmployeeAccess(Long employeeId) {
        User user = getCurrentUser();
        if (user.getRole().equalsIgnoreCase("EMPLOYEE")
                && !user.getEmployee().getId().equals(employeeId)) {
            throw new BadRequestException(
                    "Access denied: You can only access your own IPR returns");
        }
    }

    // ----------------------------------------------------------------
    // MAPPER METHODS
    // ----------------------------------------------------------------

    private IprReturnResponse mapToResponse(IprReturn iprReturn) {
        IprReturnResponse response = new IprReturnResponse();
        response.setIprId(iprReturn.getIprId());
        response.setReportingYear(iprReturn.getReportingYear());
        response.setAsOnDate(iprReturn.getAsOnDate());
        response.setTotalAnnualIncome(iprReturn.getTotalAnnualIncome());
        response.setIsNoProperty(iprReturn.getIsNoProperty());
        response.setStatus(iprReturn.getStatus());
        response.setSubmittedAt(iprReturn.getSubmittedAt());
        response.setApprovedAt(iprReturn.getApprovedAt());
        response.setCreatedAt(iprReturn.getCreatedAt());
        response.setUpdatedAt(iprReturn.getUpdatedAt());

        // Flatten employee fields by not sending Employee Object
        Employee employee = iprReturn.getEmployee();
        if (employee != null) {
            response.setEmployeeId(employee.getId());
            response.setEmployeeName(employee.getName());
            response.setEmployeeDepartment(employee.getDepartment());
            response.setEmployeePresentPostHeld(employee.getPresentPostHeld());
        }

        return response;
    }

    // ----------------------------------------------------------------
    // SERVICE METHODS
    // ----------------------------------------------------------------

    @Override
    public List<IprReturnResponse> getAllIprReturns() {
        User user = getCurrentUser();

        // Employee gets only their own returns
        if (user.getRole().equalsIgnoreCase("EMPLOYEE")) {
            return iprReturnRepository
                    .findByEmployeeId(user.getEmployee().getId())
                    .stream()
                    .map(iprReturn -> mapToResponse(iprReturn))
                    .toList();
        }

        // Admin gets all
        return iprReturnRepository.findAll()
                .stream()
                .map(iprReturn -> mapToResponse(iprReturn))
                .toList();
    }

    @Override
    public IprReturnResponse getIprReturnById(Long id) {
        IprReturn iprReturn = iprReturnRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "IPR Return not found with id: " + id));

        verifyOwnership(iprReturn);
        return mapToResponse(iprReturn);
    }

    @Override
    public List<IprReturnResponse> getIprReturnsByEmployeeId(Long employeeId) {
        verifyEmployeeAccess(employeeId);
        return iprReturnRepository.findByEmployeeId(employeeId)
                .stream()
                .map(iprReturn -> mapToResponse(iprReturn))
                .toList();
    }

    @Override
    public IprReturnResponse addIprReturn(IprReturnRequest request) {

        // Check filing window
        if (!iprNotificationService.isFilingWindowOpen()) {
            throw new BadRequestException("IPR filing window is currently closed");
        }

        // Get logged in user
        User user = getCurrentUser();
        //safety for admin --if called by admin backend cant fetch user.getEmployee() and crash
        if (user.getRole().equalsIgnoreCase("ADMIN")) {
            throw new BadRequestException("Admins cannot file IPR returns");
        }

        // Verify employee owns this — they can only file for themselves
        verifyEmployeeAccess(request.getEmployeeId());

        // Get employee from DB using employeeId sent in request
        // (already verified it matches logged in user above)
        IprReturn iprReturn = new IprReturn();
        iprReturn.setEmployee(user.getEmployee());
        iprReturn.setReportingYear(request.getReportingYear());
        iprReturn.setAsOnDate(request.getAsOnDate());
        iprReturn.setTotalAnnualIncome(request.getTotalAnnualIncome());
        iprReturn.setIsNoProperty(request.getIsNoProperty());
        iprReturn.setStatus(IprStatus.DRAFT);// always starts as DRAFT

        IprReturn saved = iprReturnRepository.save(iprReturn);
        return mapToResponse(saved);
    }

    @Override
    public IprReturnResponse updateIprReturn(Long id, IprReturnUpdateRequest request) {

        // Check filing window
        if (!iprNotificationService.isFilingWindowOpen()) {
            throw new BadRequestException("IPR filing window is currently closed");
        }

        IprReturn existing = iprReturnRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "IPR Return not found with id: " + id));

        verifyOwnership(existing);

        existing.setReportingYear(request.getReportingYear());
        existing.setAsOnDate(request.getAsOnDate());
        existing.setTotalAnnualIncome(request.getTotalAnnualIncome());
        existing.setIsNoProperty(request.getIsNoProperty());
        // status is never updated here — protected!

        IprReturn saved = iprReturnRepository.save(existing);
        return mapToResponse(saved);
    }

    @Override
    public IprReturnResponse submitIprReturn(Long id) {

        // Check filing window
        if (!iprNotificationService.isFilingWindowOpen()) {
            throw new BadRequestException("IPR filing window is currently closed");
        }

        IprReturn iprReturn = iprReturnRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "IPR Return not found with id: " + id));

        verifyOwnership(iprReturn);

        if (iprReturn.getStatus() != IprStatus.DRAFT) {
            throw new BadRequestException(
                    "Only DRAFT IPR returns can be submitted");
        }

        iprReturn.setStatus(IprStatus.SUBMITTED);
        iprReturn.setSubmittedAt(LocalDateTime.now());

        IprReturn saved = iprReturnRepository.save(iprReturn);
        return mapToResponse(saved);
    }

    @Override
    public IprReturnResponse approveIprReturn(Long id) {

        User user = getCurrentUser();

        // Only ADMIN can approve
        if (!user.getRole().equalsIgnoreCase("ADMIN")) {
            throw new BadRequestException("Only ADMIN can approve IPR returns");
        }

        IprReturn iprReturn = iprReturnRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "IPR Return not found with id: " + id));

        if (iprReturn.getStatus() != IprStatus.SUBMITTED) {
            throw new BadRequestException(
                    "Only SUBMITTED IPR returns can be approved");
        }

        iprReturn.setStatus(IprStatus.APPROVED);
        iprReturn.setApprovedAt(LocalDateTime.now());

        IprReturn saved = iprReturnRepository.save(iprReturn);
        return mapToResponse(saved);
    }

    @Override
    public void deleteIprReturn(Long id) {
        IprReturn iprReturn = iprReturnRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "IPR Return not found with id: " + id));

        verifyOwnership(iprReturn);
        iprReturnRepository.delete(iprReturn);
    }
}