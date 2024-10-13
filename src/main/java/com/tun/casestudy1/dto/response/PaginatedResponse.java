package com.tun.casestudy1.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PaginatedResponse<T> {
    List<T> content;

    int totalPages;

    long totalElements;
}