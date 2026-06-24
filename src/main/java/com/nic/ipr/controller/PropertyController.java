package com.nic.ipr.controller;

import com.nic.ipr.entity.Property;
import com.nic.ipr.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/property")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    @GetMapping("/get")
    public List<Property> getAllProperties() {
        return propertyService.getAllProperties();
    }

    @GetMapping("/get/{id}")
    public Property getPropertyById(@PathVariable Long id) {
        return propertyService.getPropertyById(id);
    }

    @GetMapping("/get/ipr/{iprId}")
    public List<Property> getPropertiesByIprId(@PathVariable Long iprId) {
        return propertyService.getPropertiesByIprId(iprId);
    }

    @PostMapping("/add")
    public Property addProperty(@RequestBody Property property) {
        return propertyService.addProperty(property);
    }

    @PutMapping("/update/{id}")
    public Property updateProperty(@PathVariable Long id,
                                   @RequestBody Property property) {
        return propertyService.updateProperty(id, property);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
    }
}
