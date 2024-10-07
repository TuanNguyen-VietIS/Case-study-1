package com.tun.casestudy1.service;

import com.tun.casestudy1.dto.response.DepartmentAchievementDto;
import com.tun.casestudy1.dto.response.EmployeeAchievementDto;
import com.tun.casestudy1.dto.response.ExcellentEmployeeDto;
import com.tun.casestudy1.entity.EmployeeRecord;
import com.tun.casestudy1.repository.EmployeeRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class EmployeeRecordServiceTest {
    @Mock
    EmployeeRecordRepository employeeRecordRepository;
    @InjectMocks
    EmployeeRecordService employeeRecordService;

    @Test
    public void testFindAll() {
        EmployeeRecord employeeRecord1 = new EmployeeRecord();
        EmployeeRecord employeeRecord2= new EmployeeRecord();
        Mockito.when(employeeRecordRepository.findAll()).thenReturn(Arrays.asList(employeeRecord1, employeeRecord2));
        List<EmployeeRecord> result = employeeRecordService.findAll();
        assertEquals(2, result.size());
    }
    @Test
    public void testFindByIdWhenEmployeeExist() {
        EmployeeRecord employeeRecord = new EmployeeRecord();
        Mockito.when(employeeRecordRepository.findById(1)).thenReturn(Optional.of(employeeRecord));
        assertEquals(employeeRecord, employeeRecordService.find(1));
    }
    @Test
    public void testFindByIdWhenEmployeeDoesNotExist() {
        Mockito.when(employeeRecordRepository.findById(1)).thenReturn(Optional.empty());
        assertEquals(null, employeeRecordService.find(1));
    }
    @Test
    public void testFindAndCountByEmployeeId() {
        List<Object[]> dataMock = Arrays.asList(
                new Object[][]{new Object[]{1, "Van Tuan", BigDecimal.valueOf(10), BigDecimal.valueOf(5), BigDecimal.valueOf(5)}}
        );
        Mockito.when(employeeRecordRepository.getEmployeeAchievementSummary()).thenReturn(dataMock);
        List<EmployeeAchievementDto> result = employeeRecordService.findAndCountByEmployeeId();
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getEmployeeId());
        assertEquals("Van Tuan", result.get(0).getName());
        assertEquals(10, result.get(0).getTotalAchievements());
        assertEquals(5, result.get(0).getTotalDisciplinary());
        assertEquals(5, result.get(0).getRewardPoints());
    }
    @Test
    public void testFindAndCountByDepartmentId() {
        List<Object[]> dataMock = Arrays.asList(
                new Object[][]{new Object[]{1, "PSX1", BigDecimal.valueOf(200), BigDecimal.valueOf(10), BigDecimal.valueOf(190)}}
        );
        Mockito.when(employeeRecordRepository.getDepartmentAchievementSummary()).thenReturn(dataMock);
        List<DepartmentAchievementDto> result = employeeRecordService.findAndCountByDepartmentId();
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getDepartmentId());
        assertEquals("PSX1", result.get(0).getName());
        assertEquals(200, result.get(0).getTotalAchievements());
        assertEquals(10, result.get(0).getTotalDisciplinary());
        assertEquals(190, result.get(0).getRewardPoints());
    }
    @Test
    void testFindExcellentEmployees() {
        List<Object[]> mockData = Arrays.asList(
                new Object[][]{new Object[]{1, "Van Tuan", "imageUrl", "IDT", BigDecimal.valueOf(15)}}
        );
        Mockito.when(employeeRecordRepository.getExcellentEmployees()).thenReturn(mockData);
        List<ExcellentEmployeeDto> result = employeeRecordService.findExcellentEmployees();
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getEmployeeId());
        assertEquals("Van Tuan", result.get(0).getName());
        assertEquals("imageUrl", result.get(0).getImageUrl());
        assertEquals("IDT", result.get(0).getNameOfDept());
        assertEquals(15, result.get(0).getTotalAchievements());
    }
}
