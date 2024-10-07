package com.tun.casestudy1.service;

import com.tun.casestudy1.dto.request.UpdateEmployeeDto;
import com.tun.casestudy1.entity.Employee;
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
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//    }

    @Test
    public void testFindAll() {
        employeeService.findAll();
        Mockito.verify(employeeRepository, Mockito.times(1)).findAll();
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
}
