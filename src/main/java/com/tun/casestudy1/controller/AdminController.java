package com.tun.casestudy1.controller;

import com.tun.casestudy1.dto.response.DepartmentAchievementDto;
import com.tun.casestudy1.dto.response.EmployeeAchievementDto;
import com.tun.casestudy1.dto.response.ExcellentEmployeeDto;
import com.tun.casestudy1.dto.request.UpdateAccountDto;
import com.tun.casestudy1.dto.request.UpdateEmployeeDto;
import com.tun.casestudy1.entity.Department;
import com.tun.casestudy1.entity.Employee;
import com.tun.casestudy1.entity.EmployeeRecord;
import com.tun.casestudy1.service.DepartmentService;
import com.tun.casestudy1.service.EmployeeRecordService;
import com.tun.casestudy1.service.EmployeeService;
import com.tun.casestudy1.service.FileStorageService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AdminController {

    EmployeeService employeeService;
    DepartmentService departmentService;
    EmployeeRecordService employeeRecordService;
    FileStorageService fileStorageService;
    MessageSource messageSource;

    // Get View

    @GetMapping("/adminHome")
    public String adminHomePage(Model model) {
        List<ExcellentEmployeeDto> excellentEmployeeDtos = employeeRecordService.findExcellentEmployees();
        model.addAttribute("excellentEmployees", excellentEmployeeDtos);
        return "admin/achievement/excellent-employee";
    }

    @GetMapping("/employee-management")
    public String getEmployeeManagementPage(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            Model model)  {
        Page<Employee> employeePage = employeeService.findPaginated(page, size);
        model.addAttribute("employees", employeePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", employeePage.getTotalPages());
        model.addAttribute("pageSize", size);

        return "admin/employee/view-list";
    }

    @GetMapping("/department-management")
    public String getDepartmentManagementPage(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            Model model) {
        Page<Department> departmentPage = departmentService.findPaginated(page, size);
        model.addAttribute("departments", departmentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", departmentPage.getTotalPages());
        model.addAttribute("pageSize", size);
        return "admin/department/view-list";
    }

    @GetMapping("/account-management")
    public String getAccountManagementPage(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            Model model) {
        Page<Employee> employeePage = employeeService.findPaginated(page, size);
        model.addAttribute("employees", employeePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", employeePage.getTotalPages());
        model.addAttribute("pageSize", size);
        return "admin/account/view-list";
    }

    @GetMapping("/view-people-achievements")
    public String getPeopleAchievements(Model model) {
        List<EmployeeAchievementDto> listAchievements = employeeRecordService.findAndCountByEmployeeId();
        model.addAttribute("listAchievements", listAchievements);
        return "admin/achievement/list-achieve-employee";
    }

    @GetMapping("/view-departments-achievements")
    public String getDepartmentsAchievements(Model model) {
        List<DepartmentAchievementDto> listAchievements = employeeRecordService.findAndCountByDepartmentId();
        model.addAttribute("listAchievements", listAchievements);
        return "admin/achievement/list-achieve-dept";
    }

    @GetMapping("/logout")
    public String logOut(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/view-list-employees-in-dept/{id}")
    public String getListEmployeesInDeptPage(@PathVariable("id") int id, Model model) {
        List<Employee> employees = employeeService.getListEmployeesInDept(id);
        model.addAttribute("employees", employees);
        return "admin/employee/view-list-employee-in-dept";
    }

    // Get Add View
    @GetMapping("/add-department")
    public String getAddDepartmentPage(Model model) {
        model.addAttribute("type", "Department");
        return "admin/department/add";
    }

    @GetMapping("/add-employee")
    public String getAddEmployeePage(Model model){
        List<Department> departments = departmentService.findAll();
        model.addAttribute("departments", departments);
        return "admin/employee/add";
    }

    @GetMapping("/create-achievement")
    public String getAddAchievementPage(Model model) {
        List<Employee> employees = employeeService.findAll();
        model.addAttribute("employees", employees);
        return "admin/achievement/add";
    }

    // Get Edit View

    @GetMapping("/edit-account/{id}")
    public String getEditAccountPage(@PathVariable("id") int id, Model model) {
        Employee employee = employeeService.find(id);
        model.addAttribute("employee", employee);
        return "admin/account/edit";
    }

    @GetMapping("/edit-department/{id}")
    public String getEditDepartmentPage(@PathVariable("id") int id, Model model) {
        Department department = departmentService.find(id);
        model.addAttribute("department", department);
        return "admin/department/edit";
    }

    @GetMapping("/edit-employee/{id}")
    public String getEditEmployeePage(@PathVariable("id") int id, Model model) {
        Employee employee = employeeService.find(id);
        model.addAttribute("employee", employee);
        List<Department> departments = departmentService.findAll();
        model.addAttribute("departments", departments);
        return "admin/employee/edit";
    }

    // Save
    @PostMapping("/add-employee")
    public String addEmployee(@Valid @ModelAttribute Employee employee,
                              @RequestParam("image-file") MultipartFile imageFile,
                              Model model, Locale locale) throws IOException {
        if (imageFile == null || imageFile.isEmpty() || imageFile.getOriginalFilename().isEmpty()) {
            employee.setImageUrl(null);
        }
        else{
            String fileName = fileStorageService.save(imageFile);
            employee.setImageUrl(fileName);
        }
         String path = fileStorageService.save(imageFile);
         employee.setImageUrl(path);

        try {
            employeeService.save(employee);
        } catch (RuntimeException e) {
            String errorMessage = messageSource.getMessage("error.duplicate", null, locale);
            model.addAttribute("errorMessage", errorMessage);

            model.addAttribute("employees", employeeService.findAll());

            List<Department> departments = departmentService.findAll();
            model.addAttribute("departments", departments);
            return "admin/employee/add";
        }

        return "redirect:/admin/employee-management";
    }

    @PostMapping("/add-department")
    public String addDepartment(@RequestParam("name") String name) {
        Department department = new Department();
        department.setName(name);
        departmentService.save(department);
        return "redirect:/admin/department-management";
    }

    @PostMapping("add-achievement")
    public String addAchivement(@ModelAttribute EmployeeRecord employeeRecord) {
        employeeRecordService.save(employeeRecord);
        return "redirect:/admin/employee-management";
    }

    // Update save

    @PostMapping("/edit-account")
    public String updateAccount(@RequestParam("id") int id,
                                @Valid @ModelAttribute UpdateAccountDto employee,
                                Model model,
                                Locale locale) {

        try {
            employeeService.updateAccount(id, employee);
        } catch (RuntimeException e) {
            String errorMessage = messageSource.getMessage("error.duplicate", null, locale);
            model.addAttribute("errorMessage", errorMessage);
            Employee employee1 = employeeService.find(id);
            model.addAttribute("employee", employee1);
            return "admin/account/edit";
        }
        return "redirect:/admin/account-management";
    }

    @PostMapping("/edit-department")
    public String updateDepartment(@RequestParam("id") int id,
                                   @RequestParam("name") String name,
                                   Model model,
                                   Locale locale) {
        try {
            departmentService.update(id, name);
        } catch (RuntimeException e) {
            String errorMessage = messageSource.getMessage("error.name.duplicate", null, locale);
            model.addAttribute("errorMessage", errorMessage);
            Department department = departmentService.find(id);
            model.addAttribute("department", department);
            return "admin/department/edit";
        }
        return "redirect:/admin/department-management";
    }

    @PostMapping("/edit-employee")
    public String updateEmployee(@RequestParam("id") int id,
                                 @Valid @ModelAttribute UpdateEmployeeDto employee,
                                 @RequestParam("image-file") MultipartFile imageFile) throws IOException {
        employeeService.updateEmployee(id, employee, imageFile);
        return "redirect:/admin/employee-management";
    }

    // Delete

    @GetMapping("/delete-account/{id}")
    public String deleteAccount(@PathVariable("id") int id) {
        employeeService.delete(id);
        return "redirect:/admin/account-management";
    }


    @GetMapping("/delete-department/{id}")
    public String deleteDepartment(@PathVariable("id") int id) {
        departmentService.delete(id);
        return "redirect:/admin/department-management";
    }


    @GetMapping("/delete-employee/{id}")
    public String deleteEmployee(@PathVariable("id") int id) {
        employeeService.delete(id);
        return "redirect:/admin/employee-management";
    }

    // Search employee
    @GetMapping("/search")
    public String searchEmployees(@RequestParam String query, Model model) {
        List<Employee> employees = employeeService.searchUser(query);
        model.addAttribute("employees", employees.isEmpty() ? new ArrayList<>() : employees);
        return "fragments/employee-fragment";
    }

    @GetMapping("/files/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) throws MalformedURLException {
        Resource file = fileStorageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +"anh" + "\"")
                .body(file);
    }

}
