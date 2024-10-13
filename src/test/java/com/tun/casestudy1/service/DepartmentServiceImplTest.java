package com.tun.casestudy1.service;

import com.tun.casestudy1.dto.request.CreateDeptRequest;
import com.tun.casestudy1.dto.response.DepartmentResponse;
import com.tun.casestudy1.dto.response.PaginatedResponse;
import com.tun.casestudy1.entity.Department;
import com.tun.casestudy1.exception.AppException;
import com.tun.casestudy1.exception.ErrorCode;
import com.tun.casestudy1.mapper.DepartmentMapper;
import com.tun.casestudy1.repository.DepartmentRepository;
import com.tun.casestudy1.service.impl.DepartmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceImplTest {

    @Mock
    DepartmentRepository departmentRepository;

    @Mock
    DepartmentMapper departmentMapper;

    @InjectMocks
    DepartmentServiceImpl departmentServiceImpl;

    @Test
    public void testDeleteDepartment() {
        departmentServiceImpl.delete(1);
        Mockito.verify(departmentRepository, Mockito.times(1)).deleteById(1);
    }

    @Test
    public void testFindAll() {
        Department department1 = Department.builder()
                .id(1)
                .name("PSX1")
                .build();

        DepartmentResponse departmentResponse1 = DepartmentResponse.builder()
                .name("PSX1")
                .build();

        Department department2 = Department.builder()
                .id(2)
                .name("IDT")
                .build();

        DepartmentResponse departmentResponse2 = DepartmentResponse.builder()
                .name("IDT")
                .build();

        List<Department> departments = Arrays.asList(department1, department2);
        Mockito.when(departmentRepository.findAll()).thenReturn(departments);
        Mockito.when(departmentMapper.toDepartmentResponse(department1)).thenReturn(departmentResponse1);
        Mockito.when(departmentMapper.toDepartmentResponse(department2)).thenReturn(departmentResponse2);

        List<DepartmentResponse> result = departmentServiceImpl.findAll();

        assertEquals(2, result.size());
        assertEquals("PSX1", result.get(0).getName());
        assertEquals("IDT", result.get(1).getName());
        Mockito.verify(departmentRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void testFindPaginated() {
        int page = 1;
        int size = 5;

        Department department1 = Department.builder()
                .id(1)
                .name("PSX1")
                .build();

        DepartmentResponse departmentResponse1 = DepartmentResponse.builder()
                .name("PSX1")
                .build();

        Department department2 = Department.builder()
                .id(2)
                .name("IDT")
                .build();

        DepartmentResponse departmentResponse2 = DepartmentResponse.builder()
                .name("IDT")
                .build();

        List<Department> departments = Arrays.asList(department1, department2);

        Page<Department> pageResult = new PageImpl<>(departments);
        Pageable pageable = PageRequest.of(page - 1, size);

        Mockito.when(departmentRepository.findAll(PageRequest.of(0, 5))).thenReturn(pageResult);
        Mockito.when(departmentMapper.toDepartmentResponse(department1)).thenReturn(departmentResponse1);
        Mockito.when(departmentMapper.toDepartmentResponse(department2)).thenReturn(departmentResponse2);

        PaginatedResponse<DepartmentResponse> result = departmentServiceImpl.findDepartmentPaginated(page, size);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals("PSX1", result.getContent().get(0).getName());
        assertEquals("IDT", result.getContent().get(1).getName());

        Mockito.verify(departmentRepository, Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void testFindById() {
        Department department = new Department();
        DepartmentResponse departmentResponse = DepartmentResponse.builder()
                .name("IDT")
                .build();

        Mockito.when(departmentRepository.findById(2)).thenReturn(Optional.of(department));
        Mockito.when(departmentMapper.toDepartmentResponse(department)).thenReturn(departmentResponse);

        DepartmentResponse foundDepartment = departmentServiceImpl.find(2);
        assertEquals(departmentResponse, foundDepartment);
        Mockito.verify(departmentRepository, Mockito.times(1)).findById(2);
    }

    @Test
    public void testFindByIdNotFound() {
        Mockito.when(departmentRepository.findById(3)).thenReturn(Optional.empty());

        Exception exception = assertThrows(AppException.class, () -> {
            departmentServiceImpl.find(3);
        });

        assertEquals(ErrorCode.NOT_EXISTED, ((AppException) exception).getErrorCode());
    }

    @Test
    public void testSave() {
        CreateDeptRequest createDeptRequest = CreateDeptRequest.builder()
                .name("PSX1")
                .build();
        Department department = Department.builder()
                .name("PSX1")
                .build();

        Mockito.when(departmentMapper.toDepartment(createDeptRequest)).thenReturn(department);

        departmentServiceImpl.save(createDeptRequest);

        Mockito.verify(departmentRepository, Mockito.times(1)).save(any(Department.class));
    }

    @Test
    public void testSaveDuplicateDepartment() {
        CreateDeptRequest createDeptRequest = new CreateDeptRequest();
        createDeptRequest.setName("PSX1");

        Mockito.when(departmentRepository.existsByName("PSX1")).thenReturn(true);

        Exception exception = assertThrows(AppException.class, () -> {
            departmentServiceImpl.save(createDeptRequest);
        });

        assertEquals(ErrorCode.DUPLICATE_NAME, ((AppException) exception).getErrorCode());
    }

    @Test
    public void testUpdateDepartment() {
        Department department = Department.builder()
                .name("Hoang Cuu Bao")
                .build();
        Mockito.when(departmentRepository.findById(2)).thenReturn(Optional.of(department));

        String updateDepartment = "Nguyen Van Tuan";
        departmentServiceImpl.update(2, updateDepartment);

        assertEquals("Nguyen Van Tuan", department.getName());
        Mockito.verify(departmentRepository, Mockito.times(1)).save(department);
    }

    @Test
    public void testUpdateDepartmentNotFound() {
        String updateDepartment = "IDT";

        Mockito.when(departmentRepository.findById(3)).thenReturn(Optional.empty());

        Exception exception = assertThrows(AppException.class, () -> {
            departmentServiceImpl.update(3, updateDepartment);
        });

        assertEquals(ErrorCode.NOT_EXISTED, ((AppException) exception).getErrorCode());
    }

    @Test
    public void testUpdate_whenNameIsDuplicate() {
        Department existingDepartment = Department.builder()
                .id(1)
                .name("PSX1")
                .build();

        Department duplicateDepartment = Department.builder()
                .id(2)
                .name("IDT")
                .build();

        Mockito.when(departmentRepository.findById(1)).thenReturn(Optional.of(existingDepartment));
        Mockito.when(departmentRepository.findByName("IDT")).thenReturn(Optional.of(duplicateDepartment));

        AppException exception = assertThrows(AppException.class, () -> {
            departmentServiceImpl.update(1, "IDT");
        });

        assertEquals(ErrorCode.DUPLICATE_NAME, exception.getErrorCode());
        Mockito.verify(departmentRepository, Mockito.times(1)).findById(1);
        Mockito.verify(departmentRepository, Mockito.times(1)).findByName("IDT");
        Mockito.verify(departmentRepository, Mockito.never()).save(any(Department.class));
    }

}
