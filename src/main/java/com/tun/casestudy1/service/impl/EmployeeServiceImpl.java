package com.tun.casestudy1.service.impl;

import com.tun.casestudy1.dto.request.CreateEmployeeRequest;
import com.tun.casestudy1.dto.request.UpdateAccountRequest;
import com.tun.casestudy1.dto.request.UpdateEmployeeRequest;
import com.tun.casestudy1.dto.response.AccountResponse;
import com.tun.casestudy1.dto.response.EmployeeResponse;
import com.tun.casestudy1.dto.response.PaginatedResponse;
import com.tun.casestudy1.entity.Employee;
import com.tun.casestudy1.exception.AppException;
import com.tun.casestudy1.exception.ErrorCode;
import com.tun.casestudy1.mapper.AccountMapper;
import com.tun.casestudy1.mapper.EmployeeMapper;
import com.tun.casestudy1.repository.EmployeeRepository;
import com.tun.casestudy1.repository.specifications.EmployeeSpecification;
import com.tun.casestudy1.service.EmployeeService;
import com.tun.casestudy1.service.FileStorageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    EmployeeRepository employeeRepository;
    FileStorageService fileStorageServiceImpl;
    AccountMapper accountMapper;
    EmployeeMapper employeeMapper;

    @Override
    public List<EmployeeResponse> findAll() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(employee -> {
                    EmployeeResponse employeeResponse = employeeMapper.toEmployeeResponse(employee);
                    if (employee.getDepartmentId() != null) {
                        employeeResponse.setDepartmentName(employee.getDepartment().getName());
                    }
                    return employeeResponse;
                })
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeResponse find(int id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED));
        EmployeeResponse employeeResponse = employeeMapper.toEmployeeResponse(employee);
        if (employee.getDepartmentId() != null) {
            employeeResponse.setDepartmentName(employee.getDepartment().getName());
        }
        return employeeResponse;
    }

    @Override
    public AccountResponse findAccount(int id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED));
        return accountMapper.toAccountResponse(employee);
    }

    @Override
    public PaginatedResponse<EmployeeResponse> findEmployeePaginated(int page, int size)  {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Employee> employeePage = employeeRepository.findAll(pageable);

        List<EmployeeResponse> employeeResponses = employeePage.getContent().stream()
                .map(employee -> {
                    EmployeeResponse employeeResponse = employeeMapper.toEmployeeResponse(employee);
                    if (employee.getDepartmentId() != null) {
                        employeeResponse.setDepartmentName(employee.getDepartment().getName());
                    }
                    return employeeResponse;
                })
                .collect(Collectors.toList());
        return PaginatedResponse.<EmployeeResponse>builder()
                .content(employeeResponses)
                .totalPages(employeePage.getTotalPages())
                .totalElements(employeePage.getTotalElements())
                .build();
    }

    @Override
    public PaginatedResponse<AccountResponse> findAccountPaginated(int page, int size)  {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Employee> employeePage = employeeRepository.findAll(pageable);

        List<AccountResponse> accountResponses = employeePage.getContent().stream()
                .map(accountMapper::toAccountResponse)
                .collect(Collectors.toList());

        return PaginatedResponse.<AccountResponse>builder()
                .content(accountResponses)
                .totalPages(employeePage.getTotalPages())
                .totalElements(employeePage.getTotalElements())
                .build();
    }

    @Override
    public void delete(int id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public void save(CreateEmployeeRequest employee) {
        boolean emailExists = employeeRepository.findByEmail(employee.getEmail()).isPresent();
        if (emailExists) {
            throw new AppException(ErrorCode.DUPLICATE_EMAIL);
        }
        Employee employee2 = employeeMapper.toEmployee(employee);
        employeeRepository.save(employee2);
    }

    @Override
    public void updateAccount(int id, UpdateAccountRequest employee) {
        Employee employee1 = employeeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED));

        Optional<Employee> existingEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (existingEmployee.isPresent() && existingEmployee.get().getId() != id) {
            throw new AppException(ErrorCode.DUPLICATE_EMAIL);
        }

        employee1.setName(employee.getName());
        employee1.setEmail(employee.getEmail());
        employee1.setPassword(employee.getPassword());

        employeeRepository.save(employee1);
    }

    @Override
    public void updateEmployee(int id, UpdateEmployeeRequest employee, MultipartFile imageFile) throws IOException {
        Employee employee1 = employeeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED));

        if (imageFile == null || imageFile.isEmpty() || imageFile.getOriginalFilename().isEmpty()) {
            employee1.setImageUrl(employee1.getImageUrl());
        }
        else{
            String fileName = fileStorageServiceImpl.save(imageFile);
            employee1.setImageUrl(fileName);
        }

        employee1.setName(employee.getName());
        employee1.setLevel(employee.getLevel());
        employee1.setPhoneNumber(employee.getPhoneNumber());
        employee1.setSalary(employee.getSalary());
        employee1.setDepartmentId(employee.getDepartmentId());
        employee1.setDOB(employee.getDOB());

        employeeRepository.save(employee1);
    }

    @Override
    public PaginatedResponse<EmployeeResponse> searchEmployees(String searchValue, String filterType, Pageable pageable) {
        Specification<Employee> spec = EmployeeSpecification.getEmployeeSpecification(searchValue, filterType);
        Page<Employee> employeePage = employeeRepository.findAll(spec, pageable);
        List<EmployeeResponse> employeeResponses = employeePage.getContent().stream()
                .map(employee -> {
                    EmployeeResponse employeeResponse = employeeMapper.toEmployeeResponse(employee);
                    if (employee.getDepartmentId() != null) {
                        employeeResponse.setDepartmentName(employee.getDepartment().getName());
                    }
                    return employeeResponse;
                })
                .collect(Collectors.toList());
        return PaginatedResponse.<EmployeeResponse>builder()
                .content(employeeResponses)
                .totalPages(employeePage.getTotalPages())
                .totalElements(employeePage.getTotalElements())
                .build();
    }

    @Override
    public List<EmployeeResponse> getListEmployeesInDept(int id) {
        List<Employee> employees = employeeRepository.findAllByDepartmentId(id);

        List<EmployeeResponse> employeeResponses = employees.stream()
                .map(employee -> {
                    EmployeeResponse employeeResponse = employeeMapper.toEmployeeResponse(employee);
                    if (employee.getDepartmentId() != null) {
                        employeeResponse.setDepartmentName(employee.getDepartment().getName());
                    }
                    return employeeResponse;
                })
                .collect(Collectors.toList());

        return employeeResponses;
    }

}
