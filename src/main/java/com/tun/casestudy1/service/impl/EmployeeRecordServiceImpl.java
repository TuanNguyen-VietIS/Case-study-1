package com.tun.casestudy1.service.impl;

import com.tun.casestudy1.dto.request.CreateEmployeeRecordRequest;
import com.tun.casestudy1.dto.response.DepartmentAchievementResponse;
import com.tun.casestudy1.dto.response.EmployeeAchievementResponse;
import com.tun.casestudy1.dto.response.EmployeeRecordResponse;
import com.tun.casestudy1.dto.response.ExcellentEmployeeResponse;
import com.tun.casestudy1.entity.EmployeeRecord;
import com.tun.casestudy1.exception.AppException;
import com.tun.casestudy1.exception.ErrorCode;
import com.tun.casestudy1.mapper.EmployeeRecordMapper;
import com.tun.casestudy1.repository.EmployeeRecordRepository;
import com.tun.casestudy1.service.EmployeeRecordService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeRecordServiceImpl implements EmployeeRecordService {

    EmployeeRecordRepository employeeRecordRepository;
    EmployeeRecordMapper employeeRecordMapper;

    @Override
    public List<EmployeeRecordResponse> findAll() {
        List<EmployeeRecord> employeeRecords = employeeRecordRepository.findAll();
        return employeeRecords.stream()
                .map(employeeRecordMapper::toEmployeeRecordResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeRecordResponse find(int id) {
        EmployeeRecord employeeRecord = employeeRecordRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED));
        return employeeRecordMapper.toEmployeeRecordResponse(employeeRecord);
    }

    @Override
    public void delete(int id) {
        employeeRecordRepository.deleteById(id);
    }

    @Override
    public void save(CreateEmployeeRecordRequest dto) {
        employeeRecordRepository.save(employeeRecordMapper.toEmployeeRecord(dto));
    }

    @Override
    public List<EmployeeAchievementResponse> findAndCountByEmployeeId() {
        return employeeRecordRepository.getEmployeeAchievementSummary();
    }

    @Override
    public List<DepartmentAchievementResponse> findAndCountByDepartmentId() {
        return employeeRecordRepository.getDepartmentAchievementSummary();
    }

    @Override
    public List<ExcellentEmployeeResponse> findExcellentEmployees() {
        return employeeRecordRepository.getExcellentEmployees();
    }
}
