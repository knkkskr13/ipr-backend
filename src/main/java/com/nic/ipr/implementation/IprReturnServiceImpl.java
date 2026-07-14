package com.nic.ipr.implementation;

import com.nic.ipr.dto.request.IprReturnRequest;
import com.nic.ipr.dto.request.IprReturnUpdateRequest;
import com.nic.ipr.dto.response.IprReturnResponse;
import com.nic.ipr.entity.Employee;
import com.nic.ipr.entity.IprReturn;
import com.nic.ipr.repository.EmployeeRepository;
import com.nic.ipr.repository.IprReturnRepository;
import com.nic.ipr.repository.PropertyRepository;
import com.nic.ipr.repository.UserRepository;
import com.nic.ipr.service.IprNotificationService;
import com.nic.ipr.service.IprReturnService;
import com.nic.ipr.service.IprWorkflowLogService;
import com.nic.ipr.shared.enums.IprStatus;
import com.nic.ipr.shared.enums.Role;
import com.nic.ipr.shared.enums.WorkflowAction;
import com.nic.ipr.shared.exception.BadRequestException;
import com.nic.ipr.shared.exception.ResourceNotFoundException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IprReturnServiceImpl implements IprReturnService {

    private final IprReturnRepository iprReturnRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final IprNotificationService notificationService;
    private final IprWorkflowLogService workflowLogService;

    // ─── JWT helpers ───────────────────────────────────────────────────────────

    private Claims getCurrentClaims() {
        return (Claims) SecurityContextHolder.getContext()
                .getAuthentication().getDetails();
    }

    private Long getCurrentUserId() {
        return getCurrentClaims().get("userId", Long.class);
    }

    private Long getCurrentEmployeeId() {
        return getCurrentClaims().get("employeeId", Long.class);
    }

    private String getCurrentRole() {
        return getCurrentClaims().get("role", String.class);
    }

    private Employee getCurrentEmployee() {
        Long employeeId = getCurrentEmployeeId();
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for current user"));
    }

    private Long getCurrentDepartmentId() {
        return getCurrentEmployee().getOffice().getDepartment().getId();
    }

    // ─── Ownership guard ───────────────────────────────────────────────────────

    /**
     * Verifies the given IprReturn belongs to the HOD's department.
     * Used in every HOD approve/return/view-by-id operation.
     */
    private void verifyBelongsToDepartment(IprReturn iprReturn) {
        Long hodDeptId = getCurrentDepartmentId();
        Long returnDeptId = iprReturn.getEmployee().getOffice().getDepartment().getId();
        if (!hodDeptId.equals(returnDeptId)) {
            throw new BadRequestException("Access denied: This IPR return does not belong to your department");
        }
    }

    /**
     * Verifies the given IprReturn belongs to the currently logged-in employee.
     * Used in every employee update/submit/delete operation.
     */
    private void verifyOwnership(IprReturn iprReturn) {
        Long myEmployeeId = getCurrentEmployeeId();
        if (!iprReturn.getEmployee().getId().equals(myEmployeeId)) {
            throw new BadRequestException("Access denied: This IPR return does not belong to you");
        }
    }

    // ─── EMPLOYEE operations ───────────────────────────────────────────────────

    @Override
    public IprReturnResponse addIprReturn(IprReturnRequest request) {
        Employee employee = getCurrentEmployee();

        // HODs file their own return too (via EmployeeController).
        // HODs skip the filing window check — their return is approved by AUTHORITY,
        // not triggered by a notification window. Only ROLE_EMPLOYEE is window-gated.
        String role = getCurrentRole();
        if (Role.ROLE_EMPLOYEE.name().equals(role)) {
            if (!notificationService.isFilingWindowOpen(employee.getOffice().getId())) {
                throw new BadRequestException("Filing window is not currently open for your office");
            }
        }

        if (iprReturnRepository.existsByEmployeeIdAndReportingYear(
                employee.getId(), request.getReportingYear())) {
            throw new BadRequestException(
                    "You have already filed an IPR return for " + request.getReportingYear());
        }

        IprReturn iprReturn = new IprReturn();
        iprReturn.setEmployee(employee);
        iprReturn.setReportingYear(request.getReportingYear());
        iprReturn.setAsOnDate(request.getAsOnDate());
        iprReturn.setTotalAnnualIncome(request.getTotalAnnualIncome());
        iprReturn.setIsNoProperty(request.getIsNoProperty());
        iprReturn.setStatus(IprStatus.DRAFT);

        IprReturn saved = iprReturnRepository.save(iprReturn);
        workflowLogService.log(saved, WorkflowAction.CREATED, getCurrentUserId(), getCurrentRole(), "IPR Return created");

        return toResponse(saved);
    }

    @Override
    public IprReturnResponse updateIprReturn(Long id, IprReturnUpdateRequest request) {
        IprReturn iprReturn = iprReturnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IPR Return not found"));

        verifyOwnership(iprReturn);

        if (iprReturn.getStatus() != IprStatus.DRAFT && iprReturn.getStatus() != IprStatus.RETURNED) {
            throw new BadRequestException("Can only update DRAFT or RETURNED returns");
        }

        iprReturn.setReportingYear(request.getReportingYear());
        iprReturn.setAsOnDate(request.getAsOnDate());
        iprReturn.setTotalAnnualIncome(request.getTotalAnnualIncome());
        iprReturn.setIsNoProperty(request.getIsNoProperty());

        return toResponse(iprReturnRepository.save(iprReturn));
    }

    @Override
    public IprReturnResponse submitIprReturn(Long id) {
        IprReturn iprReturn = iprReturnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IPR Return not found"));

        verifyOwnership(iprReturn);

        if (iprReturn.getStatus() != IprStatus.DRAFT && iprReturn.getStatus() != IprStatus.RETURNED) {
            throw new BadRequestException("Can only submit DRAFT or RETURNED returns");
        }

        if (!iprReturn.getIsNoProperty() && propertyRepository.findByIprReturnIprId(id).isEmpty()) {
            throw new BadRequestException("Please add at least one property or mark as no property");
        }

        if ("ROLE_HOD".equals(getCurrentRole())) {
            iprReturn.setStatus(IprStatus.FORWARDED);
            iprReturn.setSubmittedAt(LocalDateTime.now());
            IprReturn saved = iprReturnRepository.save(iprReturn);
            workflowLogService.log(saved, WorkflowAction.FORWARDED, getCurrentUserId(), getCurrentRole(), "IPR Return submitted and forwarded directly to Authority");
        } else {
            iprReturn.setStatus(IprStatus.SUBMITTED);
            iprReturn.setSubmittedAt(LocalDateTime.now());
            IprReturn saved = iprReturnRepository.save(iprReturn);
            workflowLogService.log(saved, WorkflowAction.SUBMITTED, getCurrentUserId(), getCurrentRole(), "IPR Return submitted");
        }
        
        return toResponse(iprReturn);
    }

    @Override
    public void deleteIprReturn(Long id) {
        IprReturn iprReturn = iprReturnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IPR Return not found"));

        verifyOwnership(iprReturn);

        if (iprReturn.getStatus() != IprStatus.DRAFT) {
            throw new BadRequestException("Can only delete DRAFT returns");
        }

        iprReturnRepository.deleteById(id);
    }

    @Override
    public List<IprReturnResponse> getMyIprReturns() {
        Long employeeId = getCurrentEmployeeId();
        return iprReturnRepository.findByEmployeeId(employeeId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public IprReturnResponse getIprReturnById(Long id) {
        return toResponse(iprReturnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IPR Return not found")));
    }

    // ─── HOD operations ────────────────────────────────────────────────────────

    @Override
    public List<IprReturnResponse> getAllIprReturnsForHodDepartment() {
        Long departmentId = getCurrentDepartmentId();
        return iprReturnRepository.findByEmployeeOfficeDepartmentId(departmentId)
                .stream()
                // HOD sees only EMPLOYEE returns in their department
                .filter(r -> r.getEmployee().getUser() != null &&
                        r.getEmployee().getUser().getRole() == Role.ROLE_EMPLOYEE)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<IprReturnResponse> getSubmittedIprReturnsForHod() {
        Long departmentId = getCurrentDepartmentId();
        return iprReturnRepository.findByStatusAndEmployeeOfficeDepartmentId(IprStatus.SUBMITTED, departmentId)
                .stream()
                .filter(r -> r.getEmployee().getUser() != null &&
                        r.getEmployee().getUser().getRole() == Role.ROLE_EMPLOYEE)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public IprReturnResponse approveIprReturn(Long id, String remarks) {
        IprReturn iprReturn = iprReturnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IPR Return not found"));

        // Department ownership check — HOD can only act on their own department's returns
        verifyBelongsToDepartment(iprReturn);

        if (iprReturn.getStatus() != IprStatus.SUBMITTED) {
            throw new BadRequestException("Can only approve SUBMITTED returns");
        }

        iprReturn.setStatus(IprStatus.FORWARDED);

        IprReturn saved = iprReturnRepository.save(iprReturn);
        workflowLogService.log(saved, WorkflowAction.FORWARDED, getCurrentUserId(), getCurrentRole(), remarks);

        return toResponse(saved);
    }

    @Override
    public IprReturnResponse returnIprReturn(Long id, String remarks) {
        IprReturn iprReturn = iprReturnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IPR Return not found"));

        // Department ownership check
        verifyBelongsToDepartment(iprReturn);

        if (iprReturn.getStatus() != IprStatus.SUBMITTED) {
            throw new BadRequestException("Can only return SUBMITTED returns");
        }

        if (remarks == null || remarks.isBlank()) {
            throw new BadRequestException("Remarks are required when returning an IPR return");
        }

        iprReturn.setStatus(IprStatus.RETURNED);

        IprReturn saved = iprReturnRepository.save(iprReturn);
        workflowLogService.log(saved, WorkflowAction.RETURNED, getCurrentUserId(), getCurrentRole(), remarks);

        return toResponse(saved);
    }

    // ─── AUTHORITY operations ──────────────────────────────────────────────────

    @Override
    public List<IprReturnResponse> getAllIprReturns() {
        return iprReturnRepository.findAll()
                .stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<IprReturnResponse> getSubmittedHodIprReturns() {
        // Authority sees SUBMITTED returns from HODs and FORWARDED returns from regular Employees
        return iprReturnRepository.findAll()
                .stream()
                .filter(r -> {
                    if (r.getEmployee().getUser() == null) return false;
                    Role role = r.getEmployee().getUser().getRole();
                    return r.getStatus() == IprStatus.FORWARDED;
                })
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public IprReturnResponse approveHodIprReturn(Long id, String remarks) {
        // Authority approves HOD returns — no department restriction needed
        IprReturn iprReturn = iprReturnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IPR Return not found"));

        if (iprReturn.getStatus() != IprStatus.SUBMITTED && iprReturn.getStatus() != IprStatus.FORWARDED) {
            throw new BadRequestException("Can only approve SUBMITTED or FORWARDED returns");
        }

        iprReturn.setStatus(IprStatus.APPROVED);
        iprReturn.setApprovedAt(LocalDateTime.now());

        IprReturn saved = iprReturnRepository.save(iprReturn);
        workflowLogService.log(saved, WorkflowAction.APPROVED, getCurrentUserId(), getCurrentRole(), remarks);

        return toResponse(saved);
    }

    @Override
    public IprReturnResponse returnHodIprReturn(Long id, String remarks) {
        IprReturn iprReturn = iprReturnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IPR Return not found"));

        if (iprReturn.getStatus() != IprStatus.SUBMITTED && iprReturn.getStatus() != IprStatus.FORWARDED) {
            throw new BadRequestException("Can only return SUBMITTED or FORWARDED returns");
        }

        if (remarks == null || remarks.isBlank()) {
            throw new BadRequestException("Remarks are required when returning an IPR return");
        }

        iprReturn.setStatus(IprStatus.RETURNED);

        IprReturn saved = iprReturnRepository.save(iprReturn);
        workflowLogService.log(saved, WorkflowAction.RETURNED, getCurrentUserId(), getCurrentRole(), remarks);

        return toResponse(saved);
    }

    // ─── Mapper ────────────────────────────────────────────────────────────────

    private IprReturnResponse toResponse(IprReturn r) {
        IprReturnResponse response = new IprReturnResponse();
        response.setIprId(r.getIprId());
        response.setEmployeeId(r.getEmployee().getId());
        response.setEmployeeName(r.getEmployee().getName());
        response.setOfficeName(r.getEmployee().getOffice().getName());
        response.setDepartmentName(r.getEmployee().getOffice().getDepartment().getName());
        response.setReportingYear(r.getReportingYear());
        response.setAsOnDate(r.getAsOnDate());
        response.setTotalAnnualIncome(r.getTotalAnnualIncome());
        response.setIsNoProperty(r.getIsNoProperty());
        response.setStatus(r.getStatus());
        response.setSubmittedAt(r.getSubmittedAt());
        response.setApprovedAt(r.getApprovedAt());
        return response;
    }
}