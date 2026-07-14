package com.nic.ipr.hod.implementation;

import com.nic.ipr.hod.dto.response.HodResponse;
import com.nic.ipr.hod.service.HodService;
import com.nic.ipr.employee.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HodServiceImpl implements HodService {

    private final UserRepository userRepository;

    @Override
    public List<HodResponse> getAllHods() {
        return userRepository.findByRole("HOD").stream()
                .filter(user -> user.getEmployee() != null)
                .map(user -> {
                    HodResponse response = new HodResponse();
                    response.setId(user.getEmployee().getId());
                    response.setName(user.getEmployee().getName());
                    response.setEmail(user.getEmployee().getEmail());
                    response.setDepartmentId(user.getEmployee().getDepartment().getId());
                    response.setDepartmentName(user.getEmployee().getDepartment().getName());

                    return response;
                })
                .toList();
    }
}