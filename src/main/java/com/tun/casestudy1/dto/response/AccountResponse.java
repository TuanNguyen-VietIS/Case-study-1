package com.tun.casestudy1.dto.response;

import com.tun.casestudy1.enums.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AccountResponse {
    int id;

    String name;

    String email;

    Role role;

    String password;

    String phoneNumber;

}
