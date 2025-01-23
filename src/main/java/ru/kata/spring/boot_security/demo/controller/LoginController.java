package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class LoginController {

    @GetMapping(value = {"/", "/login"})
    public String loginToEnter() {
        return "login";
    }
}





/*
@GetMapping(value = {"/", "/login"})
    public String login(Model model) {
        model.addAttribute("csrf", SecurityContextHolder.getContext().getAuthentication());
        return "login";
    }
 */