package ru.ndg.shop.controller;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.ndg.shop.exception.ProductNotFoundException;

@ControllerAdvice
public class AdviceController {

    @ExceptionHandler(ProductNotFoundException.class)
    public ModelAndView notFoundExceptionHandler(ProductNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("not_found");
        modelAndView.addObject("error", ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ModelAndView usernameNotFoundExceptionHandler(UsernameNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("not_found");
        modelAndView.addObject("error", ex.getMessage());
        return modelAndView;
    }
}
