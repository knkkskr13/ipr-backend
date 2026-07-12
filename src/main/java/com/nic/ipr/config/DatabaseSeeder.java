package com.nic.ipr.config;

import com.nic.ipr.hod.entity.Department;
import com.nic.ipr.employee.entity.Employee;
import com.nic.ipr.employee.entity.User;
import com.nic.ipr.hod.entity.Office;
import com.nic.ipr.hod.repository.DepartmentRepository;
import com.nic.ipr.hod.repository.OfficeRepository;
import com.nic.ipr.employee.repository.EmployeeRepository;
import com.nic.ipr.employee.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final OfficeRepository officeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Database already seeded. Skipping automated setup...");
            return;
        }

        log.info("Empty database detected. Seeding initial test data...");

        // 2. Create the Department
        Department financeDept = new Department();
        financeDept.setName("Finance Department");
        financeDept = departmentRepository.save(financeDept);

        // 3. Create the Office
        Office mainOffice = new Office();
        mainOffice.setName("Directorate of Finance");
        mainOffice.setDepartment(financeDept);
        mainOffice = officeRepository.save(mainOffice);

        // 4. Create the HOD
        Employee hodEmp = new Employee();
        hodEmp.setName("Rajesh Kumar");
        hodEmp.setEmail("rajesh.hod@tripura.gov.in");
        hodEmp.setPresentPostHeld("Joint Secretary");
        hodEmp.setDepartment(financeDept);
        hodEmp.setOffice(mainOffice);
        hodEmp = employeeRepository.save(hodEmp);

        User hodUser = new User();
        hodUser.setUsername("hod123");
        hodUser.setPassword(passwordEncoder.encode("password123"));
        hodUser.setRole("HOD");
        hodUser.setEmployee(hodEmp);
        userRepository.save(hodUser);

        // 5. Create a Standard Employee
        Employee standardEmp = new Employee();
        standardEmp.setName("Amit Saha");
        standardEmp.setEmail("amit.saha@tripura.gov.in");
        standardEmp.setPresentPostHeld("Senior IT Officer");
        standardEmp.setDepartment(financeDept);
        standardEmp.setOffice(mainOffice);
        standardEmp = employeeRepository.save(standardEmp);

        User standardUser = new User();
        standardUser.setUsername("emp123");
        standardUser.setPassword(passwordEncoder.encode("password123"));
        standardUser.setRole("EMPLOYEE");
        standardUser.setEmployee(standardEmp);
        userRepository.save(standardUser);

        log.info("Database seeding completed successfully! You can now log in.");
    }
}