package com.nic.ipr.controller;

import com.nic.ipr.dto.request.IprDeclarationRequest;
import com.nic.ipr.dto.response.IprDeclarationResponse;
import com.nic.ipr.service.IprDeclarationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/declaration")
@RequiredArgsConstructor
public class IprDeclarationController {

    private final IprDeclarationService declarationService;

    @GetMapping("/get/{iprId}")
    public IprDeclarationResponse getDeclarationByIprId(@PathVariable Long iprId) {
        return declarationService.getDeclarationByIprId(iprId);
    }

    @PostMapping("/add")
    public IprDeclarationResponse addDeclaration(
            @Valid @RequestBody IprDeclarationRequest request) {
        return declarationService.addDeclaration(request);
    }

    @PutMapping("/update/{id}")
    public IprDeclarationResponse updateDeclaration(
            @PathVariable Long id,
            @Valid @RequestBody IprDeclarationRequest request) {
        return declarationService.updateDeclaration(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteDeclaration(@PathVariable Long id) {
        declarationService.deleteDeclaration(id);
    }
}