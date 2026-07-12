package com.nic.ipr.employee.implementation;

import com.nic.ipr.employee.dto.request.IprReturnRequest;
import com.nic.ipr.employee.dto.request.IprReturnUpdateRequest;
import com.nic.ipr.employee.dto.response.IprReturnResponse;
import com.nic.ipr.employee.entity.Employee;
import com.nic.ipr.employee.entity.IprReturn;
import com.nic.ipr.employee.entity.User;
import com.nic.ipr.employee.repository.IprReturnRepository;
import com.nic.ipr.employee.repository.PropertyRepository;
import com.nic.ipr.employee.repository.UserRepository;
import com.nic.ipr.employee.service.IprReturnService;
import com.nic.ipr.employee.service.IprWorkflowLogService;
import com.nic.ipr.hod.repository.IprNotificationRepository;
import com.nic.ipr.hod.entity.IprNotification;
import com.nic.ipr.shared.exception.BadRequestException;
import com.nic.ipr.shared.exception.ResourceNotFoundException;
import com.nic.ipr.shared.status.IprStatus;
import com.nic.ipr.shared.status.WorkflowAction;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class IprReturnServiceImpl implements IprReturnService {

    private final IprReturnRepository iprReturnRepository;
    private final UserRepository userRepository;
    private final IprWorkflowLogService iprWorkflowLogService;
    private final IprNotificationRepository iprNotificationRepository;
    private final PropertyRepository propertyRepository;

    //----------- helper functions ------------------------//

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new BadRequestException("User is not authenticated");
        }
        String username = auth.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private User getCurrentHodUser() {
        User user = getCurrentUser();
        if (user.getRole() == null || !user.getRole().contains("HOD")) {
            throw new BadRequestException("Access denied: Requires HOD privileges");
        }
        if (user.getEmployee() == null) {
            throw new BadRequestException("HOD account is not assigned to an employee record.");
        }
        return user;
    }

    private void verifyOwnership(IprReturn iprReturn) {
        User user = getCurrentUser();
        if (!iprReturn.getEmployee().getId().equals(user.getEmployee().getId())) {
            throw new BadRequestException("Access denied: You can only access your own IPR returns");
        }
    }

    private void verifyDepartmentOwnership(IprReturn iprReturn, User hodUser) {
        if (iprReturn.getEmployee() == null) {
            throw new BadRequestException("Invalid IPR Return: Employee information is missing.");
        }
        Long returnDeptId = iprReturn.getEmployee().getDepartment().getId();
        Long hodDeptId = hodUser.getEmployee().getDepartment().getId();

        if (!returnDeptId.equals(hodDeptId)) {
            throw new BadRequestException("Access denied: This IPR return does not belong to your department");
        }
    }

    private Long getCurrentEmployeeOfficeId() {
        User user = getCurrentUser();
        Employee employee = user.getEmployee();
        if (employee == null) {
            throw new BadRequestException("Your account is not assigned to an employee record. Please contact your HOD.");
        }
        if (employee.getOffice() != null) {
            return employee.getOffice().getId();
        }
        if ("HOD".equals(user.getRole())) {
            return null; // HODs bypass office checks
        }
        throw new BadRequestException("Your account is not assigned to any office. Please contact your HOD.");
    }

    private boolean isFilingWindowClosed(Long officeId) {
        Optional<IprNotification> notification = iprNotificationRepository.findByOfficeIdAndActiveTrue(officeId);
        if (notification.isEmpty()) return true;
        LocalDate today = LocalDate.now();
        return today.isBefore(notification.get().getStartDate())
                || today.isAfter(notification.get().getEndDate());
    }

    private void validateAsOnDate(LocalDate asOnDate, Long officeId) {
        Optional<IprNotification> notification = iprNotificationRepository.findByOfficeIdAndActiveTrue(officeId);
        if (notification.isEmpty()) return;
        LocalDate start = notification.get().getStartDate();
        LocalDate end = notification.get().getEndDate();
        if (asOnDate.isBefore(start) || asOnDate.isAfter(end)) {
            throw new BadRequestException(
                    "As-on date must be within the filing window: " + start + " to " + end);
        }
    }

    //-----------------end of helper functions-----------------------//

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

        Employee employee = iprReturn.getEmployee();
        if (employee != null) {
            response.setEmployeeId(employee.getId());
            response.setEmployeeName(employee.getName());
            response.setEmployeeDepartment(employee.getDepartment().getName());
            response.setEmployeePresentPostHeld(employee.getPresentPostHeld());
        }
        return response;
    }

    @Override
    public List<IprReturnResponse> getAllIprReturns() {
        User user = getCurrentUser();
        return iprReturnRepository.findByEmployeeId(user.getEmployee().getId())
                .stream().map(this::mapToResponse).toList();
    }

    @Override
    public IprReturnResponse getIprReturnById(Long id) {
        IprReturn iprReturn = iprReturnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IPR Return not found with id: " + id));

        User user = getCurrentUser();

        if ("HOD".equals(user.getRole())) {
            verifyDepartmentOwnership(iprReturn, user);
        } else {
            verifyOwnership(iprReturn);
        }

        return mapToResponse(iprReturn);
    }

    @Override
    public List<IprReturnResponse> getIprReturnsByEmployeeId(Long employeeId) {
        User user = getCurrentUser();
        if (!user.getEmployee().getId().equals(employeeId)) {
            throw new BadRequestException("Access denied");
        }
        return iprReturnRepository.findByEmployeeId(employeeId)
                .stream().map(this::mapToResponse).toList();
    }

    @Override
    public IprReturnResponse addIprReturn(IprReturnRequest request) {
        User user = getCurrentUser();
        boolean isHod = "HOD".equals(user.getRole());
        Long officeId = getCurrentEmployeeOfficeId();

        if (!isHod) {
            if (officeId == null || isFilingWindowClosed(officeId)) {
                throw new BadRequestException("IPR filing window is currently closed for your office");
            }
            validateAsOnDate(request.getAsOnDate(), officeId);
        }

        if (iprReturnRepository.existsByEmployeeIdAndReportingYear(
                user.getEmployee().getId(), request.getReportingYear())) {
            throw new BadRequestException("You have already filed an IPR return for " + request.getReportingYear());
        }

        IprReturn iprReturn = new IprReturn();
        iprReturn.setEmployee(user.getEmployee());
        iprReturn.setReportingYear(request.getReportingYear());
        iprReturn.setAsOnDate(request.getAsOnDate());
        iprReturn.setTotalAnnualIncome(request.getTotalAnnualIncome());
        iprReturn.setIsNoProperty(request.getIsNoProperty());
        iprReturn.setStatus(IprStatus.DRAFT);

        IprReturn saved = iprReturnRepository.save(iprReturn);
        iprWorkflowLogService.logAction(saved, WorkflowAction.CREATED, user.getId(), user.getRole(), null);
        return mapToResponse(saved);
    }

    @Override
    public IprReturnResponse updateIprReturn(Long id, IprReturnUpdateRequest request) {
        User user = getCurrentUser();
        boolean isHod = "HOD".equals(user.getRole());
        Long officeId = getCurrentEmployeeOfficeId();

        if (!isHod) {
            if (officeId == null || isFilingWindowClosed(officeId)) {
                throw new BadRequestException("IPR filing window is currently closed for your office");
            }
        }

        IprReturn existing = iprReturnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IPR Return not found with id: " + id));
        verifyOwnership(existing);

        if (existing.getStatus() != IprStatus.DRAFT && existing.getStatus() != IprStatus.RETURNED) {
            throw new BadRequestException("You can only update a DRAFT or RETURNED IPR return");
        }

        existing.setReportingYear(request.getReportingYear());
        existing.setAsOnDate(request.getAsOnDate());
        existing.setTotalAnnualIncome(request.getTotalAnnualIncome());
        existing.setIsNoProperty(request.getIsNoProperty());

        IprReturn saved = iprReturnRepository.save(existing);
        iprWorkflowLogService.logAction(saved, WorkflowAction.UPDATED, user.getId(), user.getRole(), null);
        return mapToResponse(saved);
    }

    @Override
    public IprReturnResponse submitIprReturn(Long id) {
        User user = getCurrentUser();
        boolean isHod = "HOD".equals(user.getRole());

        if (!isHod) {
            Long officeId = getCurrentEmployeeOfficeId();
            if (isFilingWindowClosed(officeId)) {
                throw new BadRequestException("IPR filing window is currently closed for your office");
            }
        }

        IprReturn iprReturn = iprReturnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IPR Return not found with id: " + id));
        verifyOwnership(iprReturn);

        if (iprReturn.getStatus() != IprStatus.DRAFT && iprReturn.getStatus() != IprStatus.RETURNED) {
            throw new BadRequestException("Only DRAFT or RETURNED IPR returns can be submitted");
        }

        if (!iprReturn.getIsNoProperty()) {
            if (propertyRepository.findByIprReturnIprId(id).isEmpty()) {
                throw new BadRequestException("Please add at least one property before submitting, or declare no property.");
            }
        }

        WorkflowAction logAction;
        if (isHod) {
            iprReturn.setStatus(IprStatus.FORWARDED);
            iprReturn.setApprovedAt(LocalDateTime.now());
            logAction = WorkflowAction.FORWARDED;
        } else {
            iprReturn.setStatus(IprStatus.SUBMITTED);
            logAction = WorkflowAction.SUBMITTED;
        }

        iprReturn.setSubmittedAt(LocalDateTime.now());
        IprReturn saved = iprReturnRepository.save(iprReturn);

        iprWorkflowLogService.logAction(saved, logAction, user.getId(), user.getRole(), null);
        return mapToResponse(saved);
    }

    @Override
    public void deleteIprReturn(Long id) {
        User user = getCurrentUser();
        boolean isHod = "HOD".equals(user.getRole());
        Long officeId = getCurrentEmployeeOfficeId();

        if (!isHod) {
            if (officeId == null || isFilingWindowClosed(officeId)) {
                throw new BadRequestException("IPR filing window is currently closed for your office");
            }
        }

        IprReturn iprReturn = iprReturnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IPR Return not found with id: " + id));
        verifyOwnership(iprReturn);

        if (iprReturn.getStatus() != IprStatus.DRAFT) {
            throw new BadRequestException("Only DRAFT IPR returns can be deleted");
        }

        iprWorkflowLogService.logAction(iprReturn, WorkflowAction.DELETED, user.getId(), user.getRole(), null);
        iprReturnRepository.delete(iprReturn);
    }

    // ---------------------------------------------------------------
    // HOD-side operations
    // ---------------------------------------------------------------

    @Override
    public List<IprReturnResponse> getSubmittedIprReturnsForHod() {
        User hodUser = getCurrentHodUser();
        Long hodDepartmentId = hodUser.getEmployee().getDepartment().getId();
        return iprReturnRepository.findByDepartmentIdAndStatus(hodDepartmentId, IprStatus.SUBMITTED)
                .stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<IprReturnResponse> getAllIprReturnsForHodDepartment() {
        User hodUser = getCurrentHodUser();
        Long hodDepartmentId = hodUser.getEmployee().getDepartment().getId();
        return iprReturnRepository.findByDepartmentId(hodDepartmentId)
                .stream().map(this::mapToResponse).toList();
    }

    @Override
    public IprReturnResponse approveIprReturn(Long id, String remarks) {
        User hodUser = getCurrentHodUser();
        IprReturn iprReturn = iprReturnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IPR Return not found with id: " + id));
        verifyDepartmentOwnership(iprReturn, hodUser);

        if (iprReturn.getStatus() != IprStatus.SUBMITTED) {
            throw new BadRequestException("Only SUBMITTED IPR returns can be approved and forwarded");
        }

        iprReturn.setStatus(IprStatus.FORWARDED);
        iprReturn.setApprovedAt(LocalDateTime.now());

        IprReturn saved = iprReturnRepository.save(iprReturn);
        iprWorkflowLogService.logAction(saved, WorkflowAction.FORWARDED, hodUser.getId(), "HOD", remarks);
        return mapToResponse(saved);
    }

    @Override
    public IprReturnResponse returnIprReturn(Long id, String remarks) {
        User hodUser = getCurrentHodUser();
        if (remarks == null || remarks.isBlank()) {
            throw new BadRequestException("Remarks are required when returning an IPR return");
        }

        IprReturn iprReturn = iprReturnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IPR Return not found with id: " + id));
        verifyDepartmentOwnership(iprReturn, hodUser);

        if (iprReturn.getStatus() != IprStatus.SUBMITTED) {
            throw new BadRequestException("Only SUBMITTED IPR returns can be returned");
        }

        iprReturn.setStatus(IprStatus.RETURNED);

        IprReturn saved = iprReturnRepository.save(iprReturn);
        iprWorkflowLogService.logAction(saved, WorkflowAction.RETURNED, hodUser.getId(), "HOD", remarks);
        return mapToResponse(saved);
    }
}