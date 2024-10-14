package com.tun.casestudy1.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@ControllerAdvice
public class GlobalException {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationException(MethodArgumentNotValidException ex,
                                            RedirectAttributes redirectAttributes,
                                            Locale locale,
                                            HttpServletRequest request) {
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        String localizedErrorMessage = messageSource.getMessage(errorMessage, null, locale);
        redirectAttributes.addFlashAttribute("errorMessage", localizedErrorMessage);

        String referer = request.getHeader("Referer");

        return "redirect:" + referer;
    }

    @ExceptionHandler(AppException.class)
    public String handleAppException(AppException ex,
                                     RedirectAttributes redirectAttributes,
                                     Locale locale,
                                     HttpServletRequest request) {
        ErrorCode errorCode = ex.getErrorCode();
        String localizedErrorMessage = messageSource.getMessage(errorCode.getMessage(), null, locale);
        redirectAttributes.addFlashAttribute("errorMessage", localizedErrorMessage);

        String referer = request.getHeader("Referer");

        return "redirect:" + referer;
    }

}
