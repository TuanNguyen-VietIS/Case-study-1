package com.tun.casestudy1.service;

import com.tun.casestudy1.dto.request.UpdateEmployeeDto;
import com.tun.casestudy1.entity.Employee;
import com.tun.casestudy1.enums.Role;
import com.tun.casestudy1.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

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
    public void testDeleteEmployee() {
        employeeService.delete(1);
        Mockito.verify(employeeRepository, Mockito.times(1)).deleteById(1);
    }

    @Test
    public void testSearchUser() {
        String query = "John";
        employeeService.searchUser(query);
        Mockito.verify(employeeRepository, Mockito.times(1)).searchByQuery(query);
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
}
