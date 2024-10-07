package com.tun.casestudy1.controller;

import com.tun.casestudy1.dto.response.ExcellentEmployeeDto;
import com.tun.casestudy1.service.EmployeeRecordService;
import com.tun.casestudy1.service.FileStorageService;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.MalformedURLException;
import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class UserController {

    EmployeeRecordService employeeRecordService;
    FileStorageService fileStorageService;

    @GetMapping("/userHome")
    public String userHomePage(Model model) {
        List<ExcellentEmployeeDto> excellentEmployeeDtos = employeeRecordService.findExcellentEmployees();
        model.addAttribute("excellentEmployees", excellentEmployeeDtos);
        return "user/userHome";
    }

    @GetMapping("/logout")
    public String logOut(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/files/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) throws MalformedURLException {
        Resource file = fileStorageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +"anh" + "\"")
                .body(file);
    }
}
