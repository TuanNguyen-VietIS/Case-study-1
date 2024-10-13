package com.tun.casestudy1.controller;

import com.tun.casestudy1.dto.request.*;
import com.tun.casestudy1.service.EmployeeRecordService;
import com.tun.casestudy1.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/achievements")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AchievementController {

    EmployeeService employeeServiceImpl;
    EmployeeRecordService employeeRecordServiceImpl;

    @GetMapping("/view-people-achievements")
    public String getPeopleAchievements(Model model) {
        model.addAttribute("listAchievements", employeeRecordServiceImpl.findAndCountByEmployeeId());
        return "admin/achievement/list-achieve-employee";
    }

    @GetMapping("/view-departments-achievements")
    public String getDepartmentsAchievements(Model model) {
        model.addAttribute("listAchievements", employeeRecordServiceImpl.findAndCountByDepartmentId());
        return "admin/achievement/list-achieve-dept";
    }

    @GetMapping("/logout")
    public String logOut(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/create-achievement")
    public String getAddAchievementPage(Model model) {
        model.addAttribute("employees", employeeServiceImpl.findAll());
        return "admin/achievement/add";
    }

    @PostMapping("add-achievement")
    public String addAchivement(@ModelAttribute CreateEmployeeRecordRequest employeeRecord) {
        employeeRecordServiceImpl.save(employeeRecord);
        return "redirect:/admin/employees/employee-management";
    }
}
