package com.tun.casestudy1.mapper;

import com.tun.casestudy1.dto.request.CreateDeptRequest;
import com.tun.casestudy1.dto.response.DepartmentResponse;
import com.tun.casestudy1.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DepartmentMapper {

    Department toDepartment(CreateDeptRequest dto);

    DepartmentResponse toDepartmentResponse(Department department);
}
