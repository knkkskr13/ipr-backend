package com.nic.ipr.service;

import com.nic.ipr.exception.ResourceNotFoundException;
import com.nic.ipr.entity.Property;
import com.nic.ipr.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;

    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Property not found with id: " + id));
    }

    public List<Property> getPropertiesByIprId(Long iprId) {
        return propertyRepository.findByIprReturnIprId(iprId);
    }

    public Property addProperty(Property property) {
        return propertyRepository.save(property);
    }

    public Property updateProperty(Long id, Property updatedProperty) {

        Property existingProperty = propertyRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Property not found with id: " + id));

        existingProperty.setLocationAddress(updatedProperty.getLocationAddress());
        existingProperty.setPropertyType(updatedProperty.getPropertyType());
        existingProperty.setPropertyDescription(updatedProperty.getPropertyDescription());
        existingProperty.setAcquisitionCost(updatedProperty.getAcquisitionCost());
        existingProperty.setAcquisitionYear(updatedProperty.getAcquisitionYear());
        existingProperty.setPresentValue(updatedProperty.getPresentValue());
        existingProperty.setOwnerName(updatedProperty.getOwnerName());
        existingProperty.setOwnerRelation(updatedProperty.getOwnerRelation());
        existingProperty.setAcquisitionMode(updatedProperty.getAcquisitionMode());
        existingProperty.setAcquisitionDetails(updatedProperty.getAcquisitionDetails());
        existingProperty.setAnnualIncome(updatedProperty.getAnnualIncome());
        existingProperty.setRemarks(updatedProperty.getRemarks());

        return propertyRepository.save(existingProperty);
    }

    public void deleteProperty(Long id) {

        Property property = propertyRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Property not found with id: " + id));

        propertyRepository.delete(property);
    }
}