package com.tun.casestudy1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String keyword;

    String matchKeyword;

    String platform;

    String device;

    String matchingPattern;

    @OneToMany(mappedBy = "searchKeyword", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<SearchResult> searchResults = new ArrayList<>();
}
