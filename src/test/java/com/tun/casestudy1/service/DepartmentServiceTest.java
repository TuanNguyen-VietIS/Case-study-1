package com.tun.casestudy1.service;

import com.tun.casestudy1.entity.Department;
import com.tun.casestudy1.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    @Mock
    DepartmentRepository departmentRepository;

    @InjectMocks
    DepartmentService departmentService;

//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//    }

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
