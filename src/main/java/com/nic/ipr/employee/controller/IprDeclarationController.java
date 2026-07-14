package com.nic.ipr.employee.controller;

import com.nic.ipr.employee.dto.request.IprDeclarationRequest;
import com.nic.ipr.employee.dto.response.IprDeclarationResponse;
import com.nic.ipr.employee.service.IprDeclarationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employee/ipr-declaration")
@RequiredArgsConstructor
public class IprDeclarationController {

    private final IprDeclarationService iprDeclarationService;

    @GetMapping("/get/ipr/{iprId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<IprDeclarationResponse> getByIprId(@PathVariable Long iprId) {
        IprDeclarationResponse response = iprDeclarationService.getDeclarationByIprId(iprId);

        // Safely tell React: "The request worked perfectly, but there is no declaration data yet."
        if (response == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<IprDeclarationResponse> addDeclaration(@RequestBody IprDeclarationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(iprDeclarationService.addDeclaration(request));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<IprDeclarationResponse> updateDeclaration(@PathVariable Long id, @RequestBody IprDeclarationRequest request) {
        return ResponseEntity.ok(iprDeclarationService.updateDeclaration(id, request));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteDeclaration(@PathVariable Long id) {
        iprDeclarationService.deleteDeclaration(id);
        return ResponseEntity.noContent().build();
    }
}