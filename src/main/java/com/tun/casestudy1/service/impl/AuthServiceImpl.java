package com.tun.casestudy1.service.impl;

import com.tun.casestudy1.entity.Employee;
import com.tun.casestudy1.enums.Role;
import com.tun.casestudy1.repository.EmployeeRepository;
import com.tun.casestudy1.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

    EmployeeRepository employeeRepository;

    @Override
    public String authenticate(String username, String password) {
        Employee employee = employeeRepository.findByEmail(username)
                .orElse(null);
        if (employee != null && password.equals(employee.getPassword())) {
            if (employee.getRole() == Role.ADMIN) {
                return "redirect:/admin/adminHome";
            } else if (employee.getRole() == Role.USER) {
                return "redirect:/user/userHome";
            }
        }
        return null;
    }

    @Override
    public String getRoleByUsername(String username) {
        Employee employee = employeeRepository.findByEmail(username).orElse(null);
        if (employee != null) {
            return employee.getRole().name();
        }
        return null;
    }
}
