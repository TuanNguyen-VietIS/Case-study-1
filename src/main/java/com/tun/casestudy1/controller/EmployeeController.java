package com.tun.casestudy1.controller;

import com.tun.casestudy1.dto.request.*;
import com.tun.casestudy1.dto.response.*;
import com.tun.casestudy1.service.DepartmentService;
import com.tun.casestudy1.service.EmployeeService;
import com.tun.casestudy1.service.FileStorageService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/admin/employees")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class EmployeeController {

    EmployeeService employeeServiceImpl;
    DepartmentService departmentServiceImpl;
    FileStorageService fileStorageServiceImpl;
    MessageSource messageSource;

    @GetMapping("/employee-management")
    public String getEmployeeManagementPage(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            Model model)  {
        PaginatedResponse<EmployeeResponse> employeeResponses = employeeServiceImpl.findEmployeePaginated(page, size);
        model.addAttribute("employees", employeeResponses.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", employeeResponses.getTotalPages());
        model.addAttribute("pageSize", size);

        return "admin/employee/view-list";
    }

    @GetMapping("/logout")
    public String logOut(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/add-employee")
    public String getAddEmployeePage(Model model){
        model.addAttribute("departments", departmentServiceImpl.findAll());
        return "admin/employee/add";
    }

    @GetMapping("/edit-employee/{id}")
    public String getEditEmployeePage(@PathVariable("id") int id, Model model) {
        model.addAttribute("employee", employeeServiceImpl.find(id));
        model.addAttribute("departments", departmentServiceImpl.findAll());
        return "admin/employee/edit";
    }

    @PostMapping("/add-employee")
    public String addEmployee(@Valid @ModelAttribute CreateEmployeeRequest employee,
                              @RequestParam("image-file") MultipartFile imageFile,
                              Model model, Locale locale) throws IOException {
        if (imageFile == null || imageFile.isEmpty() || imageFile.getOriginalFilename().isEmpty()) {
            employee.setImageUrl(null);
        }
        else{
            String fileName = fileStorageServiceImpl.save(imageFile);
            employee.setImageUrl(fileName);
        }

        String path = fileStorageServiceImpl.save(imageFile);
        employee.setImageUrl(path);

        try {
            employeeServiceImpl.save(employee);
        } catch (RuntimeException e) {
            String errorMessage = messageSource.getMessage("error.duplicate", null, locale);
            model.addAttribute("errorMessage", errorMessage);

            model.addAttribute("employees", employeeServiceImpl.findAll());

            model.addAttribute("departments", departmentServiceImpl.findAll());
            return "admin/employee/add";
        }

        return "redirect:/admin/employees/employee-management";
    }

    @PostMapping("/edit-employee")
    public String updateEmployee(@RequestParam("id") int id,
                                 @Valid @ModelAttribute UpdateEmployeeRequest employee,
                                 @RequestParam("image-file") MultipartFile imageFile) throws IOException {
        employeeServiceImpl.updateEmployee(id, employee, imageFile);
        return "redirect:/admin/employees/employee-management";
    }

    @GetMapping("/delete-employee/{id}")
    public String deleteEmployee(@PathVariable("id") int id) {
        employeeServiceImpl.delete(id);
        return "redirect:/admin/employees/employee-management";
    }

    @GetMapping("/search")
    public ResponseEntity<String> searchEmployees(
            @RequestParam(value="query", defaultValue = "") String query,
            @RequestParam(value="filterType", defaultValue = "all") String filterType,
            @RequestParam(value="page", defaultValue = "1") int page,
            @RequestParam(value="size", defaultValue = "5") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page - 1, size);
        PaginatedResponse<EmployeeResponse> employeesPage = employeeServiceImpl.searchEmployees(query, filterType, pageable);

        model.addAttribute("employees", employeesPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", employeesPage.getTotalPages());
        model.addAttribute("pageSize", size);

        String tableBodyHtml = generateTableBodyHtml(employeesPage.getContent());

        return ResponseEntity.ok(tableBodyHtml);
    }

    private String generateTableBodyHtml(List<EmployeeResponse> employees) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringBuilder htmlBuilder = new StringBuilder();
        for (int idx = 0; idx < employees.size(); idx++) {
            EmployeeResponse employee = employees.get(idx);
            htmlBuilder.append("<tr>")
                    .append("<td>").append(idx + 1).append("</td>")
                    .append("<td><img src='/admin/files/").append(employee.getImageUrl()).append("' alt='Ảnh thẻ' style='width: 60px; height: 60px; border-radius: 50%; object-fit: cover;'/></td>")
                    .append("<td>").append(employee.getName()).append("</td>")
                    .append("<td>").append(employee.getDateOfBirth().format(formatter)).append("</td>")
                    .append("<td>").append(employee.getDepartmentName()).append("</td>")
                    .append("<td>").append(employee.getLevel()).append("</td>")
                    .append("<td>").append(employee.getPhoneNumber()).append("</td>")
                    .append("<td>").append(employee.getSalary()).append("</td>");

            if ("USER".equals(employee.getRole().name())) {
                htmlBuilder.append("<td>")
                        .append("<button onclick='location.href=\"/admin/edit-employee/").append(employee.getId()).append("\"'>Sửa</button> ")
                        .append("<button onclick='deleteEmployee(").append(employee.getId()).append(")'>Xóa</button>")
                        .append("</td>");
            } else {
                htmlBuilder.append("<td></td>");
            }
            htmlBuilder.append("</tr>");
        }
        return htmlBuilder.toString();
    }

    @GetMapping("/files/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) throws MalformedURLException {
        Resource file = fileStorageServiceImpl.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +"anh" + "\"")
                .body(file);
    }

}
