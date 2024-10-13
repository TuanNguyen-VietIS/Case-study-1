package com.tun.casestudy1.service;


import com.tun.casestudy1.dto.request.CreateDeptRequest;
import com.tun.casestudy1.dto.response.DepartmentResponse;
import com.tun.casestudy1.dto.response.PaginatedResponse;

import java.util.List;

public interface DepartmentService {
    List<DepartmentResponse> findAll();

    PaginatedResponse<DepartmentResponse> findDepartmentPaginated(int page, int size);

    DepartmentResponse find(int id);

    void delete(int id);

    void save(CreateDeptRequest dto);

    void update(int id, String name);
}
