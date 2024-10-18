package com.tun.casestudy1.repository;

import com.tun.casestudy1.entity.SearchKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchKeywordRepository extends JpaRepository<SearchKeyword, Integer> {
//    @Query("SELECT sk FROM SearchKeyword sk JOIN FETCH sk.searchResults sr WHERE FUNCTION('MONTH', sr.searchDate) = :month AND FUNCTION('YEAR', sr.searchDate) = :year")
//    List<SearchKeyword> findByMonthAndYearWithResults(@Param("month") int month, @Param("year") int year);

}
