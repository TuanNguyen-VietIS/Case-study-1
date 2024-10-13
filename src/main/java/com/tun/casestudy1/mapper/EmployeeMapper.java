package com.tun.casestudy1.mapper;

import com.tun.casestudy1.dto.request.CreateEmployeeRequest;
import com.tun.casestudy1.dto.response.EmployeeResponse;
import com.tun.casestudy1.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeMapper {

    @Mapping(source = "dateOfBirth", target = "dOB")
    Employee toEmployee(CreateEmployeeRequest dto);

    @Mapping(source = "DOB", target = "dateOfBirth")
    EmployeeResponse toEmployeeResponse(Employee employee);
}
