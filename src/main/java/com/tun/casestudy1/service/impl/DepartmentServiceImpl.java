package com.tun.casestudy1.service.impl;

import com.tun.casestudy1.dto.request.CreateDeptRequest;
import com.tun.casestudy1.dto.response.DepartmentResponse;
import com.tun.casestudy1.dto.response.PaginatedResponse;
import com.tun.casestudy1.entity.Department;
import com.tun.casestudy1.exception.AppException;
import com.tun.casestudy1.exception.ErrorCode;
import com.tun.casestudy1.mapper.DepartmentMapper;
import com.tun.casestudy1.repository.DepartmentRepository;
import com.tun.casestudy1.service.DepartmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepartmentServiceImpl implements DepartmentService {

    DepartmentRepository departmentRepository;
    DepartmentMapper departmentMapper;

    @Override
    public List<DepartmentResponse> findAll() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream()
                .map(departmentMapper::toDepartmentResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PaginatedResponse<DepartmentResponse> findDepartmentPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Department> departmentPage = departmentRepository.findAll(pageable);

        List<DepartmentResponse> departmentResponses = departmentPage.getContent().stream()
                .map(departmentMapper::toDepartmentResponse)
                .collect(Collectors.toList());

        return PaginatedResponse.<DepartmentResponse>builder()
                .content(departmentResponses)
                .totalPages(departmentPage.getTotalPages())
                .totalElements(departmentPage.getTotalElements())
                .build();
    }

    @Override
    public DepartmentResponse find(int id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED));
        return departmentMapper.toDepartmentResponse(department);
    }

    @Override
    public void delete(int id) {
        departmentRepository.deleteById(id);
    }

    @Override
    public void save(CreateDeptRequest dto) {
        if(departmentRepository.existsByName(dto.getName())) throw new AppException(ErrorCode.DUPLICATE_NAME);
        Department newDepartment = departmentMapper.toDepartment(dto);
        departmentRepository.save(newDepartment);
    }

    @Override
    public void update(int id, String name) {
        Department department1 = departmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED));

        Optional<Department> existingDepartment = departmentRepository.findByName(name);
        if (existingDepartment.isPresent() && existingDepartment.get().getId() != id) {
            throw new AppException(ErrorCode.DUPLICATE_NAME);
        }

        department1.setName(name);

        departmentRepository.save(department1);
    }
}
