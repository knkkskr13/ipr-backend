package com.nic.ipr.controller;

import com.nic.ipr.entity.IprReturn;
import com.nic.ipr.service.IprReturnService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ipr-return")
@RequiredArgsConstructor
public class IprReturnController {

    private final IprReturnService iprReturnService;

    @GetMapping("/get")
    public List<IprReturn> getAllIprReturns() {
        return iprReturnService.getAllIprReturns();
    }

    @GetMapping("/get/{id}")
    public IprReturn getIprReturnById(@PathVariable Long id) {
        return iprReturnService.getIprReturnById(id);
    }

    @GetMapping("/get/employee/{employeeId}")
    public List<IprReturn> getIprReturnsByEmployeeId(@PathVariable Long employeeId) {
        return iprReturnService.getIprReturnsByEmployeeId(employeeId);
    }

    @PostMapping("/add")
    public IprReturn addIprReturn(@RequestBody IprReturn iprReturn) {
        return iprReturnService.addIprReturn(iprReturn);
    }

    @PutMapping("/update/{id}")
    public IprReturn updateIprReturn(@PathVariable Long id,
                                     @RequestBody IprReturn iprReturn) {
        return iprReturnService.updateIprReturn(id, iprReturn);
    }

    @PutMapping("/update/{id}/submit")
    public IprReturn submitIprReturn(@PathVariable Long id) {
        return iprReturnService.submitIprReturn(id);
    }

    @PutMapping("/update/{id}/approve")
    public IprReturn approveIprReturn(@PathVariable Long id) {
        return iprReturnService.approveIprReturn(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteIprReturn(@PathVariable Long id) {
        iprReturnService.deleteIprReturn(id);
    }
}
