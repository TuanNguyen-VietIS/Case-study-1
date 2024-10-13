package com.tun.casestudy1.mapper;

import com.tun.casestudy1.dto.response.AccountResponse;
import com.tun.casestudy1.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountMapper {
    AccountResponse toAccountResponse(Employee employee);
}
