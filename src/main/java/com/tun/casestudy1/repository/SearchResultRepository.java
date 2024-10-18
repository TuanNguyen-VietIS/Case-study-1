package com.tun.casestudy1.repository;

import com.tun.casestudy1.entity.SearchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchResultRepository extends JpaRepository<SearchResult, Integer> {
}