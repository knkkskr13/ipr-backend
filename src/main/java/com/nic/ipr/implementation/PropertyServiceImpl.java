package com.nic.ipr.implementation;

import com.nic.ipr.dto.request.PropertyRequest;
import com.nic.ipr.dto.response.PropertyResponse;
import com.nic.ipr.entity.IprReturn;
import com.nic.ipr.entity.Property;
import com.nic.ipr.entity.User;
import com.nic.ipr.exception.BadRequestException;
import com.nic.ipr.exception.ResourceNotFoundException;
import com.nic.ipr.repository.IprReturnRepository;
import com.nic.ipr.repository.PropertyRepository;
import com.nic.ipr.repository.UserRepository;
import com.nic.ipr.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final IprReturnRepository iprReturnRepository;
    private final UserRepository userRepository;

    // ----------------------------------------------------------------
    // HELPER METHODS
    // ----------------------------------------------------------------

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }

    // Verify logged in employee owns the IprReturn this property belongs to
    private void verifyOwnership(IprReturn iprReturn) {
        User user = getCurrentUser();
        if (user.getRole().equalsIgnoreCase("EMPLOYEE")
                && !iprReturn.getEmployee().getId()
                .equals(user.getEmployee().getId())) {
            throw new BadRequestException(
                    "Access denied: You can only access your own properties");
        }
    }

    // ----------------------------------------------------------------
    // MAPPER METHODS
    // ----------------------------------------------------------------

    private PropertyResponse mapToResponse(Property property) {
        PropertyResponse response = new PropertyResponse();
        response.setPropertyId(property.getPropertyId());
        response.setIprId(property.getIprReturn().getIprId()); // just the ID, no nesting!
        response.setLocationAddress(property.getLocationAddress());
        response.setPropertyType(property.getPropertyType());
        response.setPropertyDescription(property.getPropertyDescription());
        response.setAcquisitionCost(property.getAcquisitionCost());
        response.setAcquisitionYear(property.getAcquisitionYear());
        response.setPresentValue(property.getPresentValue());
        response.setOwnerName(property.getOwnerName());
        response.setOwnerRelation(property.getOwnerRelation());
        response.setAcquisitionMode(property.getAcquisitionMode());
        response.setAcquisitionDetails(property.getAcquisitionDetails());
        response.setAnnualIncome(property.getAnnualIncome());
        response.setRemarks(property.getRemarks());
        response.setCreatedAt(property.getCreatedAt());
        response.setUpdatedAt(property.getUpdatedAt());
        return response;
    }

    // ----------------------------------------------------------------
    // SERVICE METHODS
    // ----------------------------------------------------------------

    @Override
    public List<PropertyResponse> getAllProperties() {
        return propertyRepository.findAll()
                .stream()
                .map(property -> mapToResponse(property))
                .toList();
    }

    @Override
    public PropertyResponse getPropertyById(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Property not found with id: " + id));

        verifyOwnership(property.getIprReturn());
        return mapToResponse(property);
    }

    @Override
    public List<PropertyResponse> getPropertiesByIprId(Long iprId) {
        IprReturn iprReturn = iprReturnRepository.findById(iprId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "IPR Return not found with id: " + iprId));

        verifyOwnership(iprReturn);

        return propertyRepository.findByIprReturnIprId(iprId)
                .stream()
                .map(property -> mapToResponse(property))
                .toList();
    }

    @Override
    public PropertyResponse addProperty(PropertyRequest request) {

        // Find the IprReturn this property belongs to
        IprReturn iprReturn = iprReturnRepository.findById(request.getIprId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "IPR Return not found with id: " + request.getIprId()));

        // Verify logged in employee owns this IprReturn
        verifyOwnership(iprReturn);

        // Map request to entity
        Property property = new Property();
        property.setIprReturn(iprReturn);           // link to IprReturn
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

        Property saved = propertyRepository.save(property);
        return mapToResponse(saved);
    }

    @Override
    public PropertyResponse updateProperty(Long id, PropertyRequest request) {

        Property existing = propertyRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Property not found with id: " + id));

        // Verify ownership using the existing property's IprReturn
        verifyOwnership(existing.getIprReturn());

        existing.setLocationAddress(request.getLocationAddress());
        existing.setPropertyType(request.getPropertyType());
        existing.setPropertyDescription(request.getPropertyDescription());
        existing.setAcquisitionCost(request.getAcquisitionCost());
        existing.setAcquisitionYear(request.getAcquisitionYear());
        existing.setPresentValue(request.getPresentValue());
        existing.setOwnerName(request.getOwnerName());
        existing.setOwnerRelation(request.getOwnerRelation());
        existing.setAcquisitionMode(request.getAcquisitionMode());
        existing.setAcquisitionDetails(request.getAcquisitionDetails());
        existing.setAnnualIncome(request.getAnnualIncome());
        existing.setRemarks(request.getRemarks());

        Property saved = propertyRepository.save(existing);
        return mapToResponse(saved);
    }

    @Override
    public void deleteProperty(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Property not found with id: " + id));

        verifyOwnership(property.getIprReturn());
        propertyRepository.delete(property);
    }
}