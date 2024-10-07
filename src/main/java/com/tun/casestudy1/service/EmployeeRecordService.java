package com.tun.casestudy1.service;

import com.tun.casestudy1.dto.response.DepartmentAchievementDto;
import com.tun.casestudy1.dto.response.EmployeeAchievementDto;
import com.tun.casestudy1.dto.response.ExcellentEmployeeDto;
import com.tun.casestudy1.entity.EmployeeRecord;
import com.tun.casestudy1.repository.EmployeeRecordRepository;
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
public class EmployeeRecordService implements IService<EmployeeRecord>{

    EmployeeRecordRepository employeeRecordRepository;

    @Override
    public List<EmployeeRecord> findAll() {
        return employeeRecordRepository.findAll();
    }

    @Override
    public EmployeeRecord find(int id) {
        return employeeRecordRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(int id) {
        employeeRecordRepository.deleteById(id);
    }

    @Override
    public void save(EmployeeRecord employeeRecord) {
        employeeRecordRepository.save(employeeRecord);
    }


    public List<EmployeeAchievementDto> findAndCountByEmployeeId() {
        List<Object[]> list = employeeRecordRepository.getEmployeeAchievementSummary();

        return list.stream()
                .map(result -> new EmployeeAchievementDto(
                        (int) result[0],
                        (String) result[1],
                        ((BigDecimal) result[2]).intValue(),
                        ((BigDecimal) result[3]).intValue(),
                        ((BigDecimal) result[4]).intValue()))
                .collect(Collectors.toList());
    }

    public List<DepartmentAchievementDto> findAndCountByDepartmentId() {
        List<Object[]> list = employeeRecordRepository.getDepartmentAchievementSummary();

        return list.stream()
                .map(result -> new DepartmentAchievementDto(
                        (int) result[0],
                        (String) result[1],
                        ((BigDecimal) result[2]).intValue(),
                        ((BigDecimal) result[3]).intValue(),
                        ((BigDecimal) result[4]).intValue()))
                .collect(Collectors.toList());
    }

    public List<ExcellentEmployeeDto> findExcellentEmployees() {
        List<Object[]> list = employeeRecordRepository.getExcellentEmployees();
        return list.stream()
                .map(result -> new ExcellentEmployeeDto(
                        (int) result[0],
                        (String) result[1],
                        (String) result[2],
                        (String) result[3],
                        ((BigDecimal) result[4]).intValue()))
                .collect(Collectors.toList());
    }
}
