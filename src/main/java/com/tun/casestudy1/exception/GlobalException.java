package com.tun.casestudy1.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

@ControllerAdvice
public class GlobalException {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationException(MethodArgumentNotValidException ex, Model model, Locale locale) {
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        String localizedErrorMessage = messageSource.getMessage(errorMessage, null, locale);
        model.addAttribute("error", localizedErrorMessage);

        return "fail";
    }

    @ExceptionHandler(AppException.class)
    public String handleAppException(AppException ex, Model model, Locale locale) {
        ErrorCode errorCode = ex.getErrorCode();
        String localizedErrorMessage = messageSource.getMessage(errorCode.getMessage(), null, locale);
        model.addAttribute("error", localizedErrorMessage);

        return "fail";
    }
}
