package com.tun.casestudy1.service;

import com.tun.casestudy1.dto.request.CreateEmployeeRecordRequest;
import com.tun.casestudy1.dto.response.DepartmentAchievementResponse;
import com.tun.casestudy1.dto.response.EmployeeAchievementResponse;
import com.tun.casestudy1.dto.response.EmployeeRecordResponse;
import com.tun.casestudy1.dto.response.ExcellentEmployeeResponse;

import java.util.List;

public interface EmployeeRecordService {
    List<EmployeeRecordResponse> findAll();

    EmployeeRecordResponse find(int id);

    void delete(int id);

    void save(CreateEmployeeRecordRequest dto);

    List<EmployeeAchievementResponse> findAndCountByEmployeeId();

    List<DepartmentAchievementResponse> findAndCountByDepartmentId();

    List<ExcellentEmployeeResponse> findExcellentEmployees();
}
