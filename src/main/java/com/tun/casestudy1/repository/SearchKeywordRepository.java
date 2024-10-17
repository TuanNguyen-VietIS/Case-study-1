package com.tun.casestudy1.repository;

import com.tun.casestudy1.entity.SearchKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchKeywordRepository extends JpaRepository<SearchKeyword, Integer> {
}
