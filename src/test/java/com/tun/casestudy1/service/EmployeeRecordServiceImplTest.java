package com.tun.casestudy1.service;

import com.tun.casestudy1.dto.request.CreateEmployeeRecordRequest;
import com.tun.casestudy1.dto.response.DepartmentAchievementResponse;
import com.tun.casestudy1.dto.response.EmployeeAchievementResponse;
import com.tun.casestudy1.dto.response.EmployeeRecordResponse;
import com.tun.casestudy1.dto.response.ExcellentEmployeeResponse;
import com.tun.casestudy1.entity.Employee;
import com.tun.casestudy1.entity.EmployeeRecord;
import com.tun.casestudy1.exception.AppException;
import com.tun.casestudy1.mapper.EmployeeRecordMapper;
import com.tun.casestudy1.repository.EmployeeRecordRepository;
import com.tun.casestudy1.service.impl.EmployeeRecordServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeRecordServiceImplTest {
    @Mock
    EmployeeRecordRepository employeeRecordRepository;

    @Mock
    EmployeeRecordMapper employeeRecordMapper;

    @InjectMocks
    EmployeeRecordServiceImpl employeeRecordServiceImpl;

    @Test
    public void testFindAll() {
        EmployeeRecord employeeRecord1 = new EmployeeRecord();
        EmployeeRecord employeeRecord2= new EmployeeRecord();
        Mockito.when(employeeRecordRepository.findAll()).thenReturn(Arrays.asList(employeeRecord1, employeeRecord2));
        List<EmployeeRecordResponse> result = employeeRecordServiceImpl.findAll();
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByIdWhenEmployeeExist() {
        EmployeeRecord employeeRecord = new EmployeeRecord();
        employeeRecord.setId(1);

        EmployeeRecordResponse employeeRecordResponse = new EmployeeRecordResponse();
        employeeRecordResponse.setId(employeeRecord.getId());

        Mockito.when(employeeRecordRepository.findById(1)).thenReturn(Optional.of(employeeRecord));
        Mockito.when(employeeRecordMapper.toEmployeeRecordResponse(employeeRecord)).thenReturn(employeeRecordResponse);

        EmployeeRecordResponse response = employeeRecordServiceImpl.find(1);

        assertNotNull(response);
        assertEquals(employeeRecord.getId(), response.getId());
    }

    @Test
    public void testFindByIdWhenEmployeeDoesNotExist() {
        Mockito.when(employeeRecordRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(AppException.class, () -> employeeRecordServiceImpl.find(1));
    }

    @Test
    public void testFindAndCountByEmployeeId() {
        EmployeeAchievementResponse achievementResponse = EmployeeAchievementResponse.builder()
                .employeeId(1)
                .name("Van Tuan")
                .totalAchievements(3L)
                .totalDisciplinary(2L)
                .rewardPoints(1L)
                .build();

        Mockito.when(employeeRecordRepository.getEmployeeAchievementSummary()).thenReturn(List.of(achievementResponse));
        List<EmployeeAchievementResponse> result = employeeRecordServiceImpl.findAndCountByEmployeeId();
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getEmployeeId());
        assertEquals("Van Tuan", result.get(0).getName());
        assertEquals(3L, result.get(0).getTotalAchievements());
        assertEquals(2L, result.get(0).getTotalDisciplinary());
        assertEquals(1L, result.get(0).getRewardPoints());
    }

    @Test
    public void testFindAndCountByDepartmentId() {
        DepartmentAchievementResponse departmentAchievementResponse = DepartmentAchievementResponse.builder()
                .departmentId(1)
                .name("PSX1")
                .totalAchievements(3L)
                .totalDisciplinary(2L)
                .rewardPoints(1L)
                .build();

        Mockito.when(employeeRecordRepository.getDepartmentAchievementSummary()).thenReturn(List.of(departmentAchievementResponse));

        List<DepartmentAchievementResponse> result = employeeRecordServiceImpl.findAndCountByDepartmentId();
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getDepartmentId());
        assertEquals("PSX1", result.get(0).getName());
        assertEquals(3L, result.get(0).getTotalAchievements());
        assertEquals(2L, result.get(0).getTotalDisciplinary());
        assertEquals(1L, result.get(0).getRewardPoints());
    }
    @Test
    void testFindExcellentEmployees() {
        ExcellentEmployeeResponse excellentEmployeeResponse = ExcellentEmployeeResponse.builder()
                .employeeId(1)
                .name("Van Tuan")
                .nameOfDept("IDT")
                .totalAchievements(3L)
                .build();

        Mockito.when(employeeRecordRepository.getExcellentEmployees()).thenReturn(List.of(excellentEmployeeResponse));
        List<ExcellentEmployeeResponse> result = employeeRecordServiceImpl.findExcellentEmployees();
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getEmployeeId());
        assertEquals("Van Tuan", result.get(0).getName());
        assertEquals("IDT", result.get(0).getNameOfDept());
        assertEquals(3L, result.get(0).getTotalAchievements());
    }

    @Test
    void testSaveEmployeeRecord() {
        CreateEmployeeRecordRequest request = new CreateEmployeeRecordRequest();
        EmployeeRecord employeeRecord = new EmployeeRecord();
        employeeRecord.setEmployee(new Employee());

        Mockito.when(employeeRecordMapper.toEmployeeRecord(request)).thenReturn(employeeRecord);

        employeeRecordServiceImpl.save(request);

        Mockito.verify(employeeRecordRepository, Mockito.times(1)).save(employeeRecord);
    }

    @Test
    public void testDeleteEmployeeRecord() {
        int idToDelete = 1;

        employeeRecordServiceImpl.delete(idToDelete);

        Mockito.verify(employeeRecordRepository).deleteById(idToDelete);
    }
}
