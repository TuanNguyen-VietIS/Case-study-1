package com.tun.casestudy1.service;

import com.tun.casestudy1.dto.request.CreateEmployeeRequest;
import com.tun.casestudy1.dto.request.UpdateAccountRequest;
import com.tun.casestudy1.dto.request.UpdateEmployeeRequest;
import com.tun.casestudy1.dto.response.AccountResponse;
import com.tun.casestudy1.dto.response.EmployeeResponse;
import com.tun.casestudy1.dto.response.PaginatedResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface EmployeeService {
    List<EmployeeResponse> findAll();

    EmployeeResponse find(int id);

    AccountResponse findAccount(int id);

    void delete(int id);

    void save(CreateEmployeeRequest dto);

    PaginatedResponse<EmployeeResponse> findEmployeePaginated(int page, int size);

    PaginatedResponse<AccountResponse> findAccountPaginated(int page, int size);

    void updateAccount(int id, UpdateAccountRequest employee);

    void updateEmployee(int id, UpdateEmployeeRequest employee, MultipartFile imageFile) throws IOException;

    PaginatedResponse<EmployeeResponse> searchEmployees(String searchValue, String filterType, Pageable pageable);

    List<EmployeeResponse> getListEmployeesInDept(int id);
}
