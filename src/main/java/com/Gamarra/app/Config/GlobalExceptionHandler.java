package com.Gamarra.app.Config;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        model.addAttribute("errorCause", ex.getCause().getMessage());
        model.addAttribute("errorMsg", ex.getMessage());
        return "error";
    }
}
