package com.tun.casestudy1.service;

import com.tun.casestudy1.entity.Department;
import com.tun.casestudy1.entity.Employee;
import com.tun.casestudy1.repository.DepartmentRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    @Mock
    DepartmentRepository departmentRepository;

    @InjectMocks
    DepartmentService departmentService;

    @Test
    public void testDeleteDepartment() {
        departmentService.delete(1);
        Mockito.verify(departmentRepository, Mockito.times(1)).deleteById(1);
    }

    @Test
    public void testFindAll() {
        departmentService.findAll();
        Mockito.verify(departmentRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void testFindPaginated() {
        int page = 1;
        int size = 5;

        Department department1 = Department.builder()
                .name("PSX1")
                .build();
        Department department2 = Department.builder()
                .name("IDT")
                .build();

        List<Department> departments = Arrays.asList(department1, department2);

        Page<Department> pageResult = new PageImpl<>(departments);
        Pageable pageable = PageRequest.of(page - 1, size);

        Mockito.when(departmentRepository.findAll(PageRequest.of(0, 5))).thenReturn(pageResult);

        Page<Department> result = departmentService.findPaginated(page, size);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals("PSX1", result.getContent().get(0).getName());

        Mockito.verify(departmentRepository, Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void testFindById() {
        Department department = new Department();
        Mockito.when(departmentRepository.findById(2)).thenReturn(Optional.of(department));

        Department foundDepartment = departmentService.find(2);
        assertEquals(department, foundDepartment);
    }

    @Test
    public void testSave() {
        Department department = Department.builder()
                .name("PSX1")
                .build();
        departmentService.save(department);
        Mockito.verify(departmentRepository, Mockito.times(1)).save(any(Department.class));
    }

    @Test
    public void testUpdateDepartment() {
        Department department = Department.builder()
                .name("Hoang Cuu Bao")
                .build();
        Mockito.when(departmentRepository.findById(2)).thenReturn(Optional.of(department));

        String updateDepartment = "Nguyen Van Tuan";
        departmentService.update(2, updateDepartment);

        assertEquals("Nguyen Van Tuan", department.getName());
        Mockito.verify(departmentRepository, Mockito.times(1)).save(department);
    }
}
