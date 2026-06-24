package com.nic.ipr.controller;

import com.nic.ipr.entity.IprDeclaration;
import com.nic.ipr.service.IprDeclarationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/declaration")
@RequiredArgsConstructor
public class IprDeclarationController {

    private final IprDeclarationService declarationService;

    @GetMapping("/get/{iprId}")
    public IprDeclaration getDeclarationByIprId(@PathVariable Long iprId) {
        return declarationService.getDeclarationByIprId(iprId);
    }

    @PostMapping("/add")
    public IprDeclaration addDeclaration(@RequestBody IprDeclaration declaration) {
        return declarationService.addDeclaration(declaration);
    }

    @PutMapping("/update/{id}")
    public IprDeclaration updateDeclaration(@PathVariable Long id,
                                            @RequestBody IprDeclaration declaration) {
        return declarationService.updateDeclaration(id, declaration);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteDeclaration(@PathVariable Long id) {
        declarationService.deleteDeclaration(id);
    }
}
