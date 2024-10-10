package com.tun.casestudy1.service;

import com.tun.casestudy1.dto.request.UpdateAccountDto;
import com.tun.casestudy1.dto.request.UpdateEmployeeDto;
import com.tun.casestudy1.entity.Employee;
import com.tun.casestudy1.enums.Role;
import com.tun.casestudy1.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    public void testFindAll() {
        employeeService.findAll();
        Mockito.verify(employeeRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void testFindPaginated() {
        int page = 1;
        int size = 5;

        Employee employee1 = Employee.builder()
                .name("Tuan")
                .email("tuan@gmail.com")
                .build();
        Employee employee2 = Employee.builder()
                .name("Tu")
                .email("tu@gmail.com")
                .build();

        List<Employee> employees = Arrays.asList(employee1, employee2);

        Page<Employee> pageResult = new PageImpl<>(employees);
        Pageable pageable = PageRequest.of(page - 1, size);

        Mockito.when(employeeRepository.findAll(PageRequest.of(0, 5))).thenReturn(pageResult);

        Page<Employee> result = employeeService.findPaginated(page, size);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals("Tuan", result.getContent().get(0).getName());

        Mockito.verify(employeeRepository, Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void testFindById() {
        Employee employee = new Employee();
        Mockito.when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));

        Employee foundEmployee = employeeService.find(1);
        assertEquals(employee, foundEmployee);
    }

    @Test
    public void testSave() {
        Employee employee = Employee.builder()
                .name("Tuan")
                .email("tuu1@gmail.com")
                .dOB(LocalDate.now())
                .build();
        Mockito.when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.empty());

        employeeService.save(employee);
        Mockito.verify(employeeRepository, Mockito.times(1)).save(any(Employee.class));
    }

    @Test
    public void testUpdateEmployee() throws IOException {
        Employee employee = Employee.builder()
                .name("Tran Thuy Trang")
                .imageUrl("image.jpg")
                .salary(200000)
                .level(5)
                .build();
        Mockito.when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));

        MultipartFile file = Mockito.mock(MultipartFile.class);
        Mockito.when(file.isEmpty()).thenReturn(true);

        UpdateEmployeeDto updateEmployeeDto = new UpdateEmployeeDto();
        updateEmployeeDto.setName("Thuy Linh");

        employeeService.updateEmployee(1, updateEmployeeDto, file);

        assertEquals("Thuy Linh", employee.getName());
        Mockito.verify(employeeRepository, Mockito.times(1)).save(employee);
    }

    @Test
    public void testUpdateAccountEmailExistsDifferentId() {
        Employee existingEmployee = new Employee();
        existingEmployee.setEmail("tuan@gmail.com");

        UpdateAccountDto updateAccountDto = new UpdateAccountDto();
        updateAccountDto.setEmail("tuan@gmail.com");
        updateAccountDto.setName("Van Tuan");
        updateAccountDto.setPassword("12345");

        Mockito.when(employeeRepository.findById(2)).thenReturn(Optional.of(existingEmployee));
        Mockito.when(employeeRepository.findByEmail(updateAccountDto.getEmail())).thenReturn(Optional.of(existingEmployee));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            employeeService.updateAccount(2, updateAccountDto);
        });

        assertEquals("Email already exists", thrown.getMessage());
    }

    @Test
    public void testDeleteEmployee() {
        employeeService.delete(1);
        Mockito.verify(employeeRepository, Mockito.times(1)).deleteById(1);
    }

    @Test
    public void testSearchEmployees_withNameFilterType() {
        String searchValue = "Nguyen Van Tuan";
        String filterType = "name";

        Pageable pageable = PageRequest.of(0, 10);

        List<Employee> employees = Collections.singletonList(new Employee());
        Page<Employee> employeePage = new PageImpl<>(employees);

        // any: Any specification is fine
        // eq: When using argument matcher for many arguments
        Mockito.when(employeeRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(employeePage);

        Page<Employee> result = employeeService.searchEmployees(searchValue, filterType, pageable);

        assertEquals(1, result.getTotalElements());
        Mockito.verify(employeeRepository, Mockito.times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    public void testGetListEmployeesInDept() {
        Employee employee1 = new Employee();
        employee1.setId(1);
        employee1.setName("Tuan Nguyen");
        employee1.setRole(Role.USER);
        employee1.setDepartmentId(10);

        Employee employee2 = new Employee();
        employee2.setId(1);
        employee2.setName("Mi xoan");
        employee2.setRole(Role.USER);
        employee1.setDepartmentId(10);

        Mockito.when(employeeRepository.findAllByDepartmentId(10)).thenReturn(Arrays.asList(employee1,employee2));

        List<Employee> result = employeeService.getListEmployeesInDept(10);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Tuan Nguyen", result.get(0).getName());
        assertEquals("Mi xoan", result.get(1).getName());
    }

    @Test
    public void testSaveEmailExists() {
        Employee employee = new Employee();
        employee.setEmail("tuan@gmail.com");

        Mockito.when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            employeeService.save(employee);
        });

        assertEquals("Existed", thrown.getMessage());
    }
}
