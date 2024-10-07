package com.tun.casestudy1.repository;

import com.tun.casestudy1.entity.EmployeeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRecordRepository extends JpaRepository<EmployeeRecord, Integer> {

    @Query(value = "SELECT r.employee_id, e.name, " +
            "SUM(CASE WHEN r.type = 1 THEN 1 ELSE 0 END) AS totalAchievements, " +
            "SUM(CASE WHEN r.type = 0 THEN 1 ELSE 0 END) AS totalDisciplines, " +
            "(SUM(CASE WHEN r.type = 1 THEN 1 ELSE 0 END) - SUM(CASE WHEN r.type = 0 THEN 1 ELSE 0 END)) AS rewardPoints " +
            "FROM employee_record r " +
            "JOIN employee e ON r.employee_id = e.id " +
            "GROUP BY r.employee_id, e.name", nativeQuery = true)
    List<Object[]> getEmployeeAchievementSummary();

    @Query(value = "SELECT d.id, d.name, " +
            "SUM(CASE WHEN r.type = 1 THEN 1 ELSE 0 END) AS totalAchievements, " +
            "SUM(CASE WHEN r.type = 0 THEN 1 ELSE 0 END) AS totalDisciplines, " +
            "(SUM(CASE WHEN r.type = 1 THEN 1 ELSE 0 END) - SUM(CASE WHEN r.type = 0 THEN 1 ELSE 0 END)) AS rewardPoints " +
            "FROM employee_record r " +
            "JOIN employee e ON r.employee_id = e.id " +
            "JOIN department d ON e.department_id = d.id " +
            "GROUP BY d.id, d.name", nativeQuery = true)
    List<Object[]> getDepartmentAchievementSummary();

    @Query(value = "SELECT e.id, e.name, e.image_url, d.name, " +
            "SUM(CASE WHEN r.type = 1 THEN 1 ELSE 0 END) AS totalAchievements " +
            "FROM employee_record r " +
            "JOIN employee e ON r.employee_id = e.id " +
            "JOIN department d ON e.department_id = d.id " +
            "GROUP BY e.id, e.name, e.image_url, d.name " +
            "HAVING totalAchievements > 0 " +
            "ORDER BY totalAchievements DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Object[]> getExcellentEmployees();
}
