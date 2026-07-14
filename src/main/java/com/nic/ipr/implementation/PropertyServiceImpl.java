package com.nic.ipr.implementation;

import com.nic.ipr.dto.request.PropertyRequest;
import com.nic.ipr.dto.response.PropertyResponse;
import com.nic.ipr.entity.IprReturn;
import com.nic.ipr.entity.Property;
import com.nic.ipr.repository.IprReturnRepository;
import com.nic.ipr.repository.PropertyRepository;
import com.nic.ipr.service.PropertyService;
import com.nic.ipr.shared.exception.BadRequestException;
import com.nic.ipr.shared.exception.ResourceNotFoundException;
import com.nic.ipr.shared.enums.IprStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final IprReturnRepository iprReturnRepository;

    @Override
    public PropertyResponse addProperty(PropertyRequest request) {
        IprReturn iprReturn = iprReturnRepository.findById(request.getIprReturnId())
                .orElseThrow(() -> new ResourceNotFoundException("IPR Return not found"));

        if (iprReturn.getStatus() != IprStatus.DRAFT && iprReturn.getStatus() != IprStatus.RETURNED) {
            throw new BadRequestException("Cannot add property to a return that is not in DRAFT or RETURNED status");
        }

        Property property = new Property();
        property.setIprReturn(iprReturn);
        property.setLocationAddress(request.getLocationAddress());
        property.setPropertyType(request.getPropertyType());
        property.setPropertyDescription(request.getPropertyDescription());
        property.setAcquisitionCost(request.getAcquisitionCost());
        property.setAcquisitionYear(request.getAcquisitionYear());
        property.setPresentValue(request.getPresentValue());
        property.setOwnerName(request.getOwnerName());
        property.setOwnerRelation(request.getOwnerRelation());
        property.setAcquisitionMode(request.getAcquisitionMode());
        property.setAcquisitionDetails(request.getAcquisitionDetails());
        property.setAnnualIncome(request.getAnnualIncome());
        property.setRemarks(request.getRemarks());

        return toResponse(propertyRepository.save(property));
    }

    @Override
    public PropertyResponse updateProperty(Long id, PropertyRequest request) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

        if (property.getIprReturn().getStatus() != IprStatus.DRAFT &&
                property.getIprReturn().getStatus() != IprStatus.RETURNED) {
            throw new BadRequestException("Cannot update property on a submitted return");
        }

        property.setLocationAddress(request.getLocationAddress());
        property.setPropertyType(request.getPropertyType());
        property.setPropertyDescription(request.getPropertyDescription());
        property.setAcquisitionCost(request.getAcquisitionCost());
        property.setAcquisitionYear(request.getAcquisitionYear());
        property.setPresentValue(request.getPresentValue());
        property.setOwnerName(request.getOwnerName());
        property.setOwnerRelation(request.getOwnerRelation());
        property.setAcquisitionMode(request.getAcquisitionMode());
        property.setAcquisitionDetails(request.getAcquisitionDetails());
        property.setAnnualIncome(request.getAnnualIncome());
        property.setRemarks(request.getRemarks());

        return toResponse(propertyRepository.save(property));
    }

    @Override
    public void deleteProperty(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

        if (property.getIprReturn().getStatus() != IprStatus.DRAFT &&
                property.getIprReturn().getStatus() != IprStatus.RETURNED) {
            throw new BadRequestException("Cannot delete property on a submitted return");
        }

        if (property.getIprReturn() != null) {
            if (property.getIprReturn().getProperties() != null) {
                property.getIprReturn().getProperties().remove(property);
            }
            property.setIprReturn(null);
        }

        propertyRepository.delete(property);
    }

    @Override
    public List<PropertyResponse> getPropertiesByIprId(Long iprId) {
        return propertyRepository.findByIprReturnIprId(iprId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    private PropertyResponse toResponse(Property p) {
        PropertyResponse response = new PropertyResponse();
        response.setId(p.getId());
        response.setIprReturnId(p.getIprReturn().getIprId());
        response.setLocationAddress(p.getLocationAddress());
        response.setPropertyType(p.getPropertyType());
        response.setPropertyDescription(p.getPropertyDescription());
        response.setAcquisitionCost(p.getAcquisitionCost());
        response.setAcquisitionYear(p.getAcquisitionYear());
        response.setPresentValue(p.getPresentValue());
        response.setOwnerName(p.getOwnerName());
        response.setOwnerRelation(p.getOwnerRelation());
        response.setAcquisitionMode(p.getAcquisitionMode());
        response.setAcquisitionDetails(p.getAcquisitionDetails());
        response.setAnnualIncome(p.getAnnualIncome());
        response.setRemarks(p.getRemarks());
        return response;
    }
}