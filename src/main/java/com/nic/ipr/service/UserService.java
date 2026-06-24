package com.nic.ipr.service;

import com.nic.ipr.entity.Employee;
import com.nic.ipr.repository.EmployeeRepository;
import com.nic.ipr.exception.BadRequestException;
import com.nic.ipr.entity.User;
import com.nic.ipr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public void registerUser(
            String username,
            String password,
            String role,
            Long employeeId
    ) {

        if (userRepository.existsByUsername(username)) {
            throw new BadRequestException("Username already exists");
        }

        User user = new User(); // makes a object of User type for setting the details and ultimately save in database

        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role.toUpperCase());

        //below line code decides only employee gets a user tag(means gets user class features) so only employee gets a employee id
        //eg. ADMIN never gets employee id
        if (role.equalsIgnoreCase("EMPLOYEE")) {// here inside if() it checks whether role is employee, this format is used to save from errors in java

            //checks if emplyoee id exists , because if absent it means admin hasnt entered his or her entry with employee id
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() ->
                            new BadRequestException("Employee not found"));

            user.setEmployee(employee);
        }

        userRepository.save(user);
    }

    /// checks in database whether the person trying to login has registered or not
    public boolean validateLogin(String username, String password) {
        Optional<User> currentUser = userRepository.findByUsername(username);
        if (!currentUser.isPresent()) {
            return false;
        }
        User user = currentUser.get();
        return passwordEncoder.matches(password, user.getPassword());
    }

}