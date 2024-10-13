package com.tun.casestudy1.service;

import com.tun.casestudy1.dto.request.CreateEmployeeRequest;
import com.tun.casestudy1.dto.request.UpdateAccountRequest;
import com.tun.casestudy1.dto.request.UpdateEmployeeRequest;
import com.tun.casestudy1.dto.response.AccountResponse;
import com.tun.casestudy1.dto.response.EmployeeResponse;
import com.tun.casestudy1.dto.response.PaginatedResponse;
import com.tun.casestudy1.entity.Department;
import com.tun.casestudy1.entity.Employee;
import com.tun.casestudy1.exception.AppException;
import com.tun.casestudy1.exception.ErrorCode;
import com.tun.casestudy1.mapper.AccountMapper;
import com.tun.casestudy1.mapper.EmployeeMapper;
import com.tun.casestudy1.repository.EmployeeRepository;
import com.tun.casestudy1.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    EmployeeMapper employeeMapper;

    @Mock
    AccountMapper accountMapper;

    @Spy
    FileStorageService fileStorageService;

    @InjectMocks
    EmployeeServiceImpl employeeServiceImpl;

    @Test
    public void testFindAll() {
        employeeServiceImpl.findAll();
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

        Mockito.when(employeeRepository.findAll(PageRequest.of(page - 1, size))).thenReturn(pageResult);

        EmployeeResponse employeeResponse1 = new EmployeeResponse();
        employeeResponse1.setName("Tuan");

        EmployeeResponse employeeResponse2 = new EmployeeResponse();
        employeeResponse2.setName("Tu");

        Mockito.when(employeeMapper.toEmployeeResponse(employee1)).thenReturn(employeeResponse1);
        Mockito.when(employeeMapper.toEmployeeResponse(employee2)).thenReturn(employeeResponse2);

        var result = employeeServiceImpl.findEmployeePaginated(page, size);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals("Tuan", result.getContent().get(0).getName());
        assertEquals("Tu", result.getContent().get(1).getName());

        Mockito.verify(employeeRepository, Mockito.times(1)).findAll(PageRequest.of(page - 1, size));
        Mockito.verify(employeeMapper, Mockito.times(2)).toEmployeeResponse(any(Employee.class));
    }

    @Test
    public void testFindAccountPaginated() {
        int page = 1;
        int size = 10;

        Employee employee1 = Employee.builder()
                .id(1)
                .name("Tuan")
                .build();

        Employee employee2 = Employee.builder()
                .id(2)
                .name("Linh")
                .build();

        List<Employee> employees = Arrays.asList(employee1, employee2);

        Page<Employee> employeePage = new PageImpl<>(employees, PageRequest.of(page - 1, size), employees.size());

        Mockito.when(employeeRepository.findAll(PageRequest.of(page - 1, size))).thenReturn(employeePage);

        AccountResponse accountResponse1 = AccountResponse.builder().id(employee1.getId()).name(employee1.getName()).build();
        AccountResponse accountResponse2 = AccountResponse.builder().id(employee2.getId()).name(employee2.getName()).build();
        List<AccountResponse> accountResponses = Arrays.asList(accountResponse1, accountResponse2);

        Mockito.when(accountMapper.toAccountResponse(employee1)).thenReturn(accountResponse1);
        Mockito.when(accountMapper.toAccountResponse(employee2)).thenReturn(accountResponse2);

        PaginatedResponse<AccountResponse> result = employeeServiceImpl.findAccountPaginated(page, size);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(2, result.getContent().size());

        assertEquals("Tuan", result.getContent().get(0).getName());
        assertEquals("Linh", result.getContent().get(1).getName());

        Mockito.verify(employeeRepository, Mockito.times(1)).findAll(PageRequest.of(page - 1, size));
        Mockito.verify(accountMapper, Mockito.times(1)).toAccountResponse(employee1);
        Mockito.verify(accountMapper, Mockito.times(1)).toAccountResponse(employee2);
    }


    @Test
    public void testFindSuccess() {
        Department department = Department.builder()
                .id(10)
                .name("IDT")
                .build();

        Employee employee = Employee.builder()
                .id(1)
                .name("Tuan")
                .email("tuan@gmail.com")
                .departmentId(10)
                .department(department)
                .build();
        Mockito.when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));

        EmployeeResponse employeeResponse = EmployeeResponse.builder()
                .name("Tuan")
                .departmentName("IDT")
                .build();

        Mockito.when(employeeMapper.toEmployeeResponse(employee)).thenReturn(employeeResponse);

        EmployeeResponse result = employeeServiceImpl.find(1);

        assertNotNull(result);
        assertEquals("Tuan", result.getName());
        assertEquals("IDT", result.getDepartmentName());

        Mockito.verify(employeeRepository, Mockito.times(1)).findById(1);
        Mockito.verify(employeeMapper, Mockito.times(1)).toEmployeeResponse(employee);
    }

    @Test
    public void testFindNotFound() {
        Mockito.when(employeeRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(AppException.class, () -> employeeServiceImpl.find(1));

        Mockito.verify(employeeRepository, Mockito.times(1)).findById(1);
    }

    @Test
    public void testFindAccountSuccess() {
        Employee employee = Employee.builder()
                .id(1)
                .name("Tuan")
                .email("tuan@gmail.com")
                .departmentId(10)
                .build();
        Mockito.when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));

        AccountResponse accountResponse = new AccountResponse();
        Mockito.when(accountMapper.toAccountResponse(employee)).thenReturn(accountResponse);

        AccountResponse result = employeeServiceImpl.findAccount(1);

        assertNotNull(result);

        Mockito.verify(employeeRepository, Mockito.times(1)).findById(1);
        Mockito.verify(accountMapper, Mockito.times(1)).toAccountResponse(employee);
    }

    @Test
    public void testFindAccountNotFound() {
        Mockito.when(employeeRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(AppException.class, () -> employeeServiceImpl.findAccount(1));

        Mockito.verify(employeeRepository, Mockito.times(1)).findById(1);
    }

    @Test
    public void testSaveSuccess() {
        CreateEmployeeRequest createEmployeeRequest = new CreateEmployeeRequest();
        createEmployeeRequest.setEmail("tuan@gmail.com");
        createEmployeeRequest.setName("Tuan");

        Mockito.when(employeeRepository.findByEmail(createEmployeeRequest.getEmail())).thenReturn(Optional.empty());

        Employee employee = new Employee();
        employee.setEmail("tuan@gmail.com");
        employee.setName("Tuan");

        Mockito.when(employeeMapper.toEmployee(createEmployeeRequest)).thenReturn(employee);

        employeeServiceImpl.save(createEmployeeRequest);

        Mockito.verify(employeeRepository, Mockito.times(1)).save(employee);
    }

    @Test
    public void testSaveEmailExists() {
        CreateEmployeeRequest createEmployeeRequest = new CreateEmployeeRequest();
        createEmployeeRequest.setEmail("tuan@gmail.com");
        createEmployeeRequest.setName("Tuan");

        Mockito.when(employeeRepository.findByEmail(createEmployeeRequest.getEmail())).thenReturn(Optional.of(new Employee()));

        Exception exception = assertThrows(AppException.class, () -> {
            employeeServiceImpl.save(createEmployeeRequest);
        });

        assertEquals(ErrorCode.DUPLICATE_EMAIL, ((AppException) exception).getErrorCode());

        Mockito.verify(employeeRepository, Mockito.never()).save(any());
    }

    @Test
    public void testUpdateEmployee() throws IOException {
        UpdateEmployeeRequest updateEmployeeRequest = UpdateEmployeeRequest.builder()
                .name("Tuan Nguyen")
                .phoneNumber("0123456789")
                .departmentId(10)
                .build();

        Employee existingEmployee = Employee.builder()
                .id(1)
                .name("Tuan")
                .phoneNumber("0987654321")
                .departmentId(5)
                .imageUrl("old_image_url.jpg")
                .build();

        Mockito.when(employeeRepository.findById(1)).thenReturn(Optional.of(existingEmployee));

        employeeServiceImpl.updateEmployee(1, updateEmployeeRequest, null);

        assertEquals("Tuan Nguyen", existingEmployee.getName());
        assertEquals("0123456789", existingEmployee.getPhoneNumber());
        assertEquals(10, existingEmployee.getDepartmentId());
        assertEquals("old_image_url.jpg", existingEmployee.getImageUrl());

        Mockito.verify(employeeRepository, Mockito.times(1)).save(existingEmployee);
    }

    @Test
    public void testUpdateAccountSuccess() {
        UpdateAccountRequest updateAccountRequest = UpdateAccountRequest.builder()
                .email("tuan@gmail.com")
                .name("Tuan Nguyen")
                .password("newPassword")
                .build();

        Employee existingEmployee = Employee.builder()
                .id(1)
                .name("Tuan")
                .email("tuan_old@gmail.com")
                .password("oldPassword")
                .build();

        Mockito.when(employeeRepository.findById(1)).thenReturn(Optional.of(existingEmployee));
        Mockito.when(employeeRepository.findByEmail(updateAccountRequest.getEmail())).thenReturn(Optional.empty());

        employeeServiceImpl.updateAccount(1, updateAccountRequest);

        assertEquals("Tuan Nguyen", existingEmployee.getName());
        assertEquals("tuan@gmail.com", existingEmployee.getEmail());
        assertEquals("newPassword", existingEmployee.getPassword());

        Mockito.verify(employeeRepository, Mockito.times(1)).save(existingEmployee);
    }

    @Test
    public void testUpdateAccountEmailExistsDifferentId() {
        Employee existingEmployee = new Employee();
        existingEmployee.setEmail("tuan@gmail.com");

        UpdateAccountRequest updateAccountDto = new UpdateAccountRequest();
        updateAccountDto.setEmail("tuan@gmail.com");
        updateAccountDto.setName("Van Tuan");
        updateAccountDto.setPassword("12345");

        Mockito.when(employeeRepository.findById(2)).thenReturn(Optional.of(existingEmployee));
        Mockito.when(employeeRepository.findByEmail(updateAccountDto.getEmail())).thenReturn(Optional.of(existingEmployee));

        Exception exception = assertThrows(AppException.class, () -> {
            employeeServiceImpl.updateAccount(2, updateAccountDto);
        });

        assertEquals(ErrorCode.DUPLICATE_EMAIL, ((AppException) exception).getErrorCode());
    }

    @Test
    public void testDeleteEmployee() {
        employeeServiceImpl.delete(1);
        Mockito.verify(employeeRepository, Mockito.times(1)).deleteById(1);
    }

    @Test
    public void testSearchEmployees_withNameFilterType() {
        String searchValue = "Nguyen Van Tuan";
        String filterType = "name";
        Pageable pageable = PageRequest.of(0, 10);

        Department department = Department.builder()
                .id(1)
                .name("IDT")
                .build();

        Employee employee = Employee.builder()
                .id(1)
                .name("Nguyen Van Tuan")
                .departmentId(1)
                .department(department)
                .build();

        List<Employee> employees = Collections.singletonList(employee);
        Page<Employee> employeePage = new PageImpl<>(employees, pageable, employees.size());

        Mockito.when(employeeRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(employeePage);

        EmployeeResponse employeeResponse = EmployeeResponse.builder()
                .id(employee.getId())
                .name(employee.getName())
                .departmentName("IDT")
                .build();

        Mockito.when(employeeMapper.toEmployeeResponse(employee)).thenReturn(employeeResponse);

        PaginatedResponse<EmployeeResponse> result = employeeServiceImpl.searchEmployees(searchValue, filterType, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals("Nguyen Van Tuan", result.getContent().get(0).getName());
        assertEquals("IDT", result.getContent().get(0).getDepartmentName());

        Mockito.verify(employeeRepository, Mockito.times(1)).findAll(any(Specification.class), eq(pageable));
        Mockito.verify(employeeMapper, Mockito.times(1)).toEmployeeResponse(employee);
    }


    @Test
    public void testGetListEmployeesInDept() {
        Department department1 = Department.builder()
                .id(1)
                .name("IDT")
                .build();

        Employee employee1 = Employee.builder()
                .id(1)
                .name("Nguyen Van Tuan")
                .departmentId(1)
                .department(department1)
                .build();

        Employee employee2 = Employee.builder()
                .id(2)
                .name("Nguyen Yen Linh")
                .departmentId(2)
                .department(department1)
                .build();

        Mockito.when(employeeRepository.findAllByDepartmentId(1)).thenReturn(Arrays.asList(employee1, employee2));

        EmployeeResponse response1 = EmployeeResponse.builder()
                .id(employee1.getId())
                .name(employee1.getName())
                .build();

        EmployeeResponse response2 = EmployeeResponse.builder()
                .id(employee2.getId())
                .name(employee2.getName())
                .build();

        Mockito.when(employeeMapper.toEmployeeResponse(employee1)).thenReturn(response1);
        Mockito.when(employeeMapper.toEmployeeResponse(employee2)).thenReturn(response2);

        List<EmployeeResponse> employeeResponses = employeeServiceImpl.getListEmployeesInDept(1);

        assertEquals(2, employeeResponses.size());
        assertEquals("Nguyen Van Tuan", employeeResponses.get(0).getName());
        assertEquals("IDT", employeeResponses.get(0).getDepartmentName());
        assertEquals("Nguyen Yen Linh", employeeResponses.get(1).getName());
        assertEquals("IDT", employeeResponses.get(1).getDepartmentName());

        Mockito.verify(employeeRepository, Mockito.times(1)).findAllByDepartmentId(1);
        Mockito.verify(employeeMapper, Mockito.times(1)).toEmployeeResponse(employee1);
        Mockito.verify(employeeMapper, Mockito.times(1)).toEmployeeResponse(employee2);
    }

}
