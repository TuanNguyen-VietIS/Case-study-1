package com.tun.casestudy1.controller;

import com.tun.casestudy1.dto.request.*;
import com.tun.casestudy1.dto.response.*;
import com.tun.casestudy1.service.DepartmentService;
import com.tun.casestudy1.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Controller
@RequestMapping("/admin/departments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class DepartmentController {

    EmployeeService employeeServiceImpl;
    DepartmentService departmentServiceImpl;
    MessageSource messageSource;

    @GetMapping("/department-management")
    public String getDepartmentManagementPage(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            Model model) {
        PaginatedResponse<DepartmentResponse> departmentRespones = departmentServiceImpl.findDepartmentPaginated(page, size);
        model.addAttribute("departments", departmentRespones.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", departmentRespones.getTotalPages());
        model.addAttribute("pageSize", size);
        return "admin/department/view-list";
    }

    @GetMapping("/logout")
    public String logOut(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/view-list-employees-in-dept/{id}")
    public String getListEmployeesInDeptPage(@PathVariable("id") int id, Model model) {
        model.addAttribute("employees", employeeServiceImpl.getListEmployeesInDept(id));
        return "admin/employee/view-list-employee-in-dept";
    }

    // Get Add View
    @GetMapping("/add-department")
    public String getAddDepartmentPage() {
        return "admin/department/add";
    }

    @GetMapping("/add-employee")
    public String getAddEmployeePage(Model model){
        model.addAttribute("departments", departmentServiceImpl.findAll());
        return "admin/employee/add";
    }

    @GetMapping("/edit-department/{id}")
    public String getEditDepartmentPage(@PathVariable("id") int id, Model model) {
        model.addAttribute("department", departmentServiceImpl.find(id));
        return "admin/department/edit";
    }

    @PostMapping("/add-department")
    public String addDepartment(@ModelAttribute CreateDeptRequest department) {
        departmentServiceImpl.save(department);
        return "redirect:/admin/departments/department-management";
    }

    @PostMapping("/edit-department")
    public String updateDepartment(@RequestParam("id") int id,
                                   @RequestParam("name") String name,
                                   Model model,
                                   Locale locale) {
        try {
            departmentServiceImpl.update(id, name);
        } catch (RuntimeException e) {
            String errorMessage = messageSource.getMessage("error.name.duplicate", null, locale);
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("department", departmentServiceImpl.find(id));
            return "admin/department/edit";
        }
        return "redirect:/admin/departments/department-management";
    }

    @GetMapping("/delete-department/{id}")
    public String deleteDepartment(@PathVariable("id") int id) {
        departmentServiceImpl.delete(id);
        return "redirect:/admin/departments/department-management";
    }
}
