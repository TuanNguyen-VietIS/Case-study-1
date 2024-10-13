package com.tun.casestudy1.controller;

import com.tun.casestudy1.dto.request.*;
import com.tun.casestudy1.dto.response.*;
import com.tun.casestudy1.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Controller
@RequestMapping("/admin/accounts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AccountController {

    EmployeeService employeeServiceImpl;
    MessageSource messageSource;

    @GetMapping("/account-management")
    public String getAccountManagementPage(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            Model model) {
        PaginatedResponse<AccountResponse> accountPage = employeeServiceImpl.findAccountPaginated(page, size);
        model.addAttribute("accounts", accountPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", accountPage.getTotalPages());
        model.addAttribute("pageSize", size);
        return "admin/account/view-list";
    }

    @GetMapping("/logout")
    public String logOut(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/edit-account/{id}")
    public String getEditAccountPage(@PathVariable("id") int id, Model model) {
        model.addAttribute("employee", employeeServiceImpl.findAccount(id));
        return "admin/account/edit";
    }

    @PostMapping("/edit-account")
    public String updateAccount(@RequestParam("id") int id,
                                @Valid @ModelAttribute UpdateAccountRequest employee,
                                Model model,
                                Locale locale) {
        try {
            employeeServiceImpl.updateAccount(id, employee);
        } catch (RuntimeException e) {
            String errorMessage = messageSource.getMessage("error.duplicate", null, locale);
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("employee", employeeServiceImpl.findAccount(id));
            return "admin/account/edit";
        }
        return "redirect:/admin/accounts/account-management";
    }

    @GetMapping("/delete-account/{id}")
    public String deleteAccount(@PathVariable("id") int id) {
        employeeServiceImpl.delete(id);
        return "redirect:/admin/accounts/account-management";
    }
}
