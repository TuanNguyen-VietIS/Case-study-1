package com.tun.casestudy1.controller;

import com.tun.casestudy1.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Locale;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class HomeController {

    AuthService authService;
    MessageSource messageSource;

    @GetMapping({"/", "/login"})
    public String getLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(String username, String password, HttpSession session, Model model, Locale locale) {
        String viewName = authService.authenticate(username, password);

        if (viewName != null) {
            String role = authService.getRoleByUsername(username);
            if (role != null) {
                session.setAttribute("userRole", role);
            }
            return viewName;
        }

        model.addAttribute("error", messageSource.getMessage("login.error", null, locale));
        return "login";
    }

    @GetMapping("/error1")
    public String handleError() {
        return "access_denied";
    }

}
