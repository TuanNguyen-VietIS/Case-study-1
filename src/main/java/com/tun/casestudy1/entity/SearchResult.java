package com.tun.casestudy1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchResult {
    @Id
    @GeneratedValue(strategy = GenerationType .IDENTITY)
    int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "search_keyword_id", insertable = false, updatable = false)
    SearchKeyword searchKeyword;

    @Column(name = "search_keyword_id")
    int searchKeywordId;

    String screenshotPath;

    @Column(columnDefinition = "TEXT")
    String suggestions;

    LocalDate searchDate;

    int found;
}
