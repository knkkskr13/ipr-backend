package com.nic.ipr.employee.implementation;

import com.nic.ipr.employee.dto.request.PropertyRequest;
import com.nic.ipr.employee.dto.response.PropertyResponse;
import com.nic.ipr.employee.entity.IprReturn;
import com.nic.ipr.employee.entity.Property;
import com.nic.ipr.employee.entity.User;
import com.nic.ipr.employee.repository.IprReturnRepository;
import com.nic.ipr.employee.repository.PropertyRepository;
import com.nic.ipr.employee.repository.UserRepository;
import com.nic.ipr.employee.service.PropertyService;
import com.nic.ipr.shared.exception.BadRequestException;
import com.nic.ipr.shared.exception.ResourceNotFoundException;
import com.nic.ipr.shared.status.IprStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final IprReturnRepository iprReturnRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new BadRequestException("User is not authenticated");
        }
        String username = auth.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private PropertyResponse mapToResponse(Property property) {
        PropertyResponse response = new PropertyResponse();
        response.setId(property.getId());

        if (property.getIprReturn() != null) {
            response.setIprReturnId(property.getIprReturn().getIprId());
        }

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

    private void mapRequestToProperty(Property property, PropertyRequest request) {
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
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropertyResponse> getPropertiesByIprId(Long iprId) {
        // Removed the temporary debugging try-catch block with printStackTrace
        List<Property> properties = propertyRepository.findByIprReturnIprId(iprId);

        return properties.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PropertyResponse addProperty(PropertyRequest request) {
        IprReturn iprReturn = iprReturnRepository.findById(request.getIprReturnId())
                .orElseThrow(() -> new ResourceNotFoundException("IPR Return not found"));

        User user = getCurrentUser();
        if (!iprReturn.getEmployee().getId().equals(user.getEmployee().getId())) {
            throw new BadRequestException("Access denied: This IPR return does not belong to you");
        }

        if (iprReturn.getStatus() != IprStatus.DRAFT && iprReturn.getStatus() != IprStatus.RETURNED) {
            throw new BadRequestException("You can only modify properties of a DRAFT or RETURNED IPR return");
        }

        Property property = new Property();
        property.setIprReturn(iprReturn);
        mapRequestToProperty(property, request);

        return mapToResponse(propertyRepository.save(property));
    }

    @Override
    public PropertyResponse updateProperty(Long id, PropertyRequest request) {
        Property existing = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));

        User user = getCurrentUser();
        if (!existing.getIprReturn().getEmployee().getId().equals(user.getEmployee().getId())) {
            throw new BadRequestException("Access denied: This IPR return does not belong to you");
        }

        if (existing.getIprReturn().getStatus() != IprStatus.DRAFT &&
                existing.getIprReturn().getStatus() != IprStatus.RETURNED) {
            throw new BadRequestException("You can only update properties of a DRAFT or RETURNED IPR return");
        }

        mapRequestToProperty(existing, request);

        return mapToResponse(propertyRepository.save(existing));
    }

    @Override
    public void deleteProperty(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));

        User user = getCurrentUser();
        if (!property.getIprReturn().getEmployee().getId().equals(user.getEmployee().getId())) {
            throw new BadRequestException("Access denied: This IPR return does not belong to you");
        }

        if (property.getIprReturn().getStatus() != IprStatus.DRAFT && property.getIprReturn().getStatus() != IprStatus.RETURNED) {
            throw new BadRequestException("You can only delete properties of a DRAFT or RETURNED IPR return");
        }

        propertyRepository.deleteById(id);
    }
}