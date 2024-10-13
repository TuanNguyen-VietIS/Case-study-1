package com.tun.casestudy1.controller;

import com.tun.casestudy1.service.EmployeeRecordService;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AdminController {

    EmployeeRecordService employeeRecordServiceImpl;

    @GetMapping("/adminHome")
    public String adminHomePage(Model model) {
        model.addAttribute("excellentEmployees", employeeRecordServiceImpl.findExcellentEmployees());
        return "admin/achievement/excellent-employee";
    }

    @GetMapping("/logout")
    public String logOut(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
