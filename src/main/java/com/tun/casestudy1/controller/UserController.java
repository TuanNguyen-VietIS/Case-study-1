package com.tun.casestudy1.controller;

import com.tun.casestudy1.dto.response.ExcellentEmployeeResponse;
import com.tun.casestudy1.service.impl.EmployeeRecordServiceImpl;
import com.tun.casestudy1.service.impl.FileStorageServiceImpl;
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

    EmployeeRecordServiceImpl employeeRecordServiceImpl;
    FileStorageServiceImpl fileStorageServiceImpl;

    @GetMapping("/userHome")
    public String userHomePage(Model model) {
        List<ExcellentEmployeeResponse> excellentEmployeeResponses = employeeRecordServiceImpl.findExcellentEmployees();
        model.addAttribute("excellentEmployees", excellentEmployeeResponses);
        return "user/userHome";
    }

    @GetMapping("/logout")
    public String logOut(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/files/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) throws MalformedURLException {
        Resource file = fileStorageServiceImpl.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +"anh" + "\"")
                .body(file);
    }
}
