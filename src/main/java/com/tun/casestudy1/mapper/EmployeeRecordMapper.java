package com.tun.casestudy1.mapper;

import com.tun.casestudy1.dto.request.CreateEmployeeRecordRequest;
import com.tun.casestudy1.dto.response.EmployeeRecordResponse;
import com.tun.casestudy1.entity.EmployeeRecord;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeRecordMapper {
    EmployeeRecord toEmployeeRecord(CreateEmployeeRecordRequest dto);

    EmployeeRecordResponse toEmployeeRecordResponse(EmployeeRecord employeeRecord);
}
