package com.unknownbrain.recipeapp.controllers;

import com.unknownbrain.recipeapp.exceptions.NotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.servlet.ModelAndView;

@Log4j2
@ControllerAdvice
public class ControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NumberFormatException.class)
    public String handleNumberFormatException(Model model, Exception exception) {

        log.error("Handling Number Format exception");
        log.error("exception", exception);

//        ModelAndView modelAndView = new ModelAndView();

//        modelAndView.setViewName("400error");
//        modelAndView.addObject("exception", exception);
//
//        return modelAndView;
        model.addAttribute("exception", exception);
        return "400error";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(Model model, Exception exception) {

        log.error("Handling not found exception");
        log.error("exception", exception);

//        ModelAndView modelAndView = new ModelAndView();

//        modelAndView.setViewName("404error");
//        modelAndView.addObject("exception", exception);

//        return modelAndView;

        model.addAttribute("exception", exception);
        return "404error";
    }
}
