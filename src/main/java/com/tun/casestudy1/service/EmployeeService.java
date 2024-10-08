package com.tun.casestudy1.service;

import com.tun.casestudy1.dto.request.UpdateAccountDto;
import com.tun.casestudy1.dto.request.UpdateEmployeeDto;
import com.tun.casestudy1.entity.Employee;
import com.tun.casestudy1.repository.EmployeeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeService implements IService<Employee>{

    EmployeeRepository employeeRepository;
    FileStorageService fileStorageService;

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee find(int id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public Page<Employee> findPaginated(int page, int size)  {
        Pageable pageable = PageRequest.of(page - 1, size);
        return employeeRepository.findAll(pageable);

    }

    @Override
    public void delete(int id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public void save(Employee employee) {
        boolean emailExists = employeeRepository.findByEmail(employee.getEmail()).isPresent();
        if (emailExists) {
            throw new RuntimeException("Existed");
        }

        Employee employee2 = Employee.builder()
                .name(employee.getName())
                .email(employee.getEmail())
                .password(employee.getPassword())
                .gender(employee.getGender())
                .salary(employee.getSalary())
                .level(employee.getLevel())
                .phoneNumber(employee.getPhoneNumber())
                .note(employee.getNote())
                .imageUrl(employee.getImageUrl())
                .dOB(employee.getDOB())
                .departmentId(employee.getDepartmentId())
                .build();
        employeeRepository.save(employee2);
    }

    public void updateAccount(int id, UpdateAccountDto employee) {
        Employee employee1 = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not Found"));

        Optional<Employee> existingEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (existingEmployee.isPresent() && existingEmployee.get().getId() != id) {
            throw new RuntimeException("Email already exists");
        }

        employee1.setName(employee.getName());
        employee1.setEmail(employee.getEmail());
        employee1.setPassword(employee.getPassword());

        employeeRepository.save(employee1);
    }

    public void updateEmployee(int id, UpdateEmployeeDto employee, MultipartFile imageFile) throws IOException {
        Employee employee1 = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not Found"));

        if (imageFile == null || imageFile.isEmpty() || imageFile.getOriginalFilename().isEmpty()) {
            employee1.setImageUrl(employee1.getImageUrl());
        }
        else{
            String fileName = fileStorageService.save(imageFile);
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

    public List<Employee> searchUser(String query) {
        return employeeRepository.searchByQuery(query);
    }

    public List<Employee> getListEmployeesInDept(int id) {
        return employeeRepository.findAllByDepartmentId(id);
    }

}
