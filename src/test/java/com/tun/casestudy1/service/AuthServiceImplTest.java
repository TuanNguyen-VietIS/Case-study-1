package com.tun.casestudy1.service;

import com.tun.casestudy1.entity.Employee;
import com.tun.casestudy1.enums.Role;
import com.tun.casestudy1.repository.EmployeeRepository;
import com.tun.casestudy1.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    AuthServiceImpl authServiceImpl;

    @Test
    public void testAdminAuthenticationSuccess() {
        Employee employee = new Employee();
        employee.setEmail("admin@gmail.com");
        employee.setPassword("12345");
        employee.setRole(Role.ADMIN);

        Mockito.when(employeeRepository.findByEmail("admin@gmail.com")).thenReturn(Optional.of(employee));

        String result = authServiceImpl.authenticate("admin@gmail.com", "12345");

        assertEquals("redirect:/admin/adminHome", result);
    }

    @Test
    public void testUserAuthenticationSuccess() {
        Employee employee = new Employee();
        employee.setEmail("h@gmail.com");
        employee.setPassword("333");
        employee.setRole(Role.USER);

        Mockito.when(employeeRepository.findByEmail("h@gmail.com")).thenReturn(Optional.of(employee));

        String result = authServiceImpl.authenticate("h@gmail.com", "333");

        assertEquals("redirect:/user/userHome", result);
    }

    @Test
    public void testAuthenticationFail() {
        Mockito.when(employeeRepository.findByEmail("test@gmail.com")).thenReturn(Optional.empty());

        String result = authServiceImpl.authenticate("test@gmail.com", "111");

        assertNull(result);
    }

    @Test
    public void testWrongPassword() {
        Employee employee = new Employee();
        employee.setEmail("h@gmail.com");
        employee.setPassword("333");
        employee.setRole(Role.USER);

        Mockito.when(employeeRepository.findByEmail("h@gmail.com")).thenReturn(Optional.of(employee));

        String result = authServiceImpl.authenticate("h@gmail.com", "111");

        assertNull(result);
    }

    @Test
    public void testGetRoleByUserName() {
        Employee employee = new Employee();
        employee.setEmail("test@gmail.com");
        employee.setRole(Role.ADMIN);
        Mockito.when(employeeRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(employee));
        String role = authServiceImpl.getRoleByUsername("test@gmail.com");
        assertEquals("ADMIN", role);
    }

    @Test
    public void testGetRoleByUsername_NotFound() {
        Mockito.when(employeeRepository.findByEmail("notfound@gmail.com")).thenReturn(Optional.empty());
        String role = authServiceImpl.getRoleByUsername("notfound@gmail.com");
        assertNull(role);
    }
}
