package com.nic.ipr.controller;

import com.nic.ipr.dto.request.*;
import com.nic.ipr.dto.response.*;
import com.nic.ipr.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authority")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_AUTHORITY')")
public class AuthorityController {

    private final DepartmentService departmentService;
    private final OfficeService officeService;
    private final EmployeeService employeeService;
    private final UserService userService;
    private final IprReturnService iprReturnService;
    private final PropertyService propertyService;
    private final IprDeclarationService declarationService;
    private final IprWorkflowLogService workflowLogService;

    // -------------------- DEPARTMENT --------------------//

    @PostMapping("/department/add")
    public ResponseEntity<DepartmentResponse> addDepartment(@Valid @RequestBody DepartmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(departmentService.addDepartment(request));
    }

    @PutMapping("/department/update/{id}")
    public ResponseEntity<DepartmentResponse> updateDepartment(@PathVariable Long id, @Valid @RequestBody DepartmentRequest request) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, request));
    }

    @DeleteMapping("/department/delete/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/department/get")
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @GetMapping("/department/get/{id}")
    public ResponseEntity<DepartmentResponse> getDepartmentById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    // -------------------- OFFICE --------------------//

    @PostMapping("/office/add")
    public ResponseEntity<OfficeResponse> addOffice(@Valid @RequestBody OfficeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(officeService.addOffice(request));
    }

    @PutMapping("/office/update/{id}")
    public ResponseEntity<OfficeResponse> updateOffice(@PathVariable Long id, @Valid @RequestBody OfficeRequest request) {
        return ResponseEntity.ok(officeService.updateOffice(id, request));
    }

    @DeleteMapping("/office/delete/{id}")
    public ResponseEntity<Void> deleteOffice(@PathVariable Long id) {
        officeService.deleteOffice(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/office/get")
    public ResponseEntity<List<OfficeResponse>> getAllOffices() {
        return ResponseEntity.ok(officeService.getAllOffices());
    }

    @GetMapping("/office/get/{id}")
    public ResponseEntity<OfficeResponse> getOfficeById(@PathVariable Long id) {
        return ResponseEntity.ok(officeService.getOfficeById(id));
    }

    @GetMapping("/office/get/department/{departmentId}")
    public ResponseEntity<List<OfficeResponse>> getOfficesByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(officeService.getOfficesByDepartmentId(departmentId));
    }

    // -------------------- EMPLOYEE --------------------//

    @PostMapping("/employee/add")
    public ResponseEntity<EmployeeResponse> addEmployee(@Valid @RequestBody EmployeeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.addEmployee(request));
    }

    @PutMapping("/employee/update/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeRequest request) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, request));
    }

    @DeleteMapping("/employee/delete/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employee/get")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/employee/get/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    // -------------------- USER CREDENTIALS --------------------//

    @PostMapping("/user/create")
    public ResponseEntity<UserResponse> createCredentials(@Valid @RequestBody UserCredentialRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createCredentials(request));
    }

    @PutMapping("/user/update/{id}")
    public ResponseEntity<UserResponse> updateCredentials(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(userService.updateCredentials(id, request));
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/get")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // -------------------- HOD IPR RETURNS --------------------//

    @GetMapping("/ipr-return/get")
    public ResponseEntity<List<IprReturnResponse>> getAllIprReturns() {
        return ResponseEntity.ok(iprReturnService.getAllIprReturns());
    }

    @GetMapping("/ipr-return/get/{id}")
    public ResponseEntity<IprReturnResponse> getIprReturnById(@PathVariable Long id) {
        return ResponseEntity.ok(iprReturnService.getIprReturnById(id));
    }

    @GetMapping("/ipr-return/hod/pending")
    public ResponseEntity<List<IprReturnResponse>> getPendingHodReturns() {
        return ResponseEntity.ok(iprReturnService.getSubmittedHodIprReturns());
    }

    @PutMapping("/ipr-return/approve/{id}")
    public ResponseEntity<IprReturnResponse> approveHodReturn(@PathVariable Long id,
                                                              @RequestBody(required = false) IprReturnDecisionRequest request) {
        String remarks = request != null ? request.getRemarks() : null;
        return ResponseEntity.ok(iprReturnService.approveHodIprReturn(id, remarks));
    }

    @PutMapping("/ipr-return/return/{id}")
    public ResponseEntity<IprReturnResponse> returnHodReturn(@PathVariable Long id,
                                                             @Valid @RequestBody IprReturnDecisionRequest request) {
        return ResponseEntity.ok(iprReturnService.returnHodIprReturn(id, request.getRemarks()));
    }

    // -------------------- PROPERTIES & DECLARATION --------------------//

    @GetMapping("/property/get/{iprId}")
    public ResponseEntity<List<PropertyResponse>> getPropertiesByIprId(@PathVariable Long iprId) {
        return ResponseEntity.ok(propertyService.getPropertiesByIprId(iprId));
    }

    @GetMapping("/declaration/get/{iprId}")
    public ResponseEntity<IprDeclarationResponse> getDeclarationByIprId(@PathVariable Long iprId) {
        return ResponseEntity.ok(declarationService.getDeclarationByIprId(iprId));
    }

    // -------------------- WORKFLOW LOG --------------------//

    @GetMapping("/workflow-log/get/{iprId}")
    public ResponseEntity<List<IprWorkflowLogResponse>> getWorkflowLog(@PathVariable Long iprId) {
        return ResponseEntity.ok(workflowLogService.getLogsByIprId(iprId));
    }
}