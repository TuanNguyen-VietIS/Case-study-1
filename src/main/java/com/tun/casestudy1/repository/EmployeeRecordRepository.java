package com.tun.casestudy1.repository;

import com.tun.casestudy1.dto.response.DepartmentAchievementResponse;
import com.tun.casestudy1.dto.response.EmployeeAchievementResponse;
import com.tun.casestudy1.dto.response.ExcellentEmployeeResponse;
import com.tun.casestudy1.entity.EmployeeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRecordRepository extends JpaRepository<EmployeeRecord, Integer> {

    @Query("SELECT new com.tun.casestudy1.dto.response.EmployeeAchievementResponse(e.id, e.name, " +
            "SUM(CASE WHEN r.type = 1 THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN r.type = 0 THEN 1 ELSE 0 END), " +
            "(SUM(CASE WHEN r.type = 1 THEN 1 ELSE 0 END) - SUM(CASE WHEN r.type = 0 THEN 1 ELSE 0 END))) " +
            "FROM EmployeeRecord r " +
            "JOIN Employee e ON r.employee.id = e.id " +
            "GROUP BY e.id, e.name")
    List<EmployeeAchievementResponse> getEmployeeAchievementSummary();


    @Query(value = "SELECT new com.tun.casestudy1.dto.response.DepartmentAchievementResponse( " +
            "d.id, d.name, " +
            "SUM(CASE WHEN r.type = 1 THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN r.type = 0 THEN 1 ELSE 0 END), " +
            "(SUM(CASE WHEN r.type = 1 THEN 1 ELSE 0 END) - SUM(CASE WHEN r.type = 0 THEN 1 ELSE 0 END)) ) " +
            "FROM EmployeeRecord r " +
            "JOIN Employee e ON r.employee.id = e.id " +
            "JOIN Department d ON e.department.id = d.id " +
            "GROUP BY d.id, d.name")
    List<DepartmentAchievementResponse> getDepartmentAchievementSummary();

    @Query(value = "SELECT new com.tun.casestudy1.dto.response.ExcellentEmployeeResponse( " +
            "e.id, e.name, e.imageUrl, d.name, " +
            "SUM(CASE WHEN r.type = 1 THEN 1 ELSE 0 END) ) " +
            "FROM EmployeeRecord r " +
            "JOIN Employee e ON r.employee.id = e.id " +
            "JOIN Department d ON e.department.id = d.id " +
            "GROUP BY e.id, e.name, e.imageUrl, d.name " +
            "HAVING SUM(CASE WHEN r.type = 1 THEN 1 ELSE 0 END) > 0 " +
            "ORDER BY SUM(CASE WHEN r.type = 1 THEN 1 ELSE 0 END) DESC " +
            "LIMIT 10")
    List<ExcellentEmployeeResponse> getExcellentEmployees();

}
