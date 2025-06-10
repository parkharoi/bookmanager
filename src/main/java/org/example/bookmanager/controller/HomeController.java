package org.example.bookmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "adminHome";
    }

    @GetMapping("/search")
    public String searchPage() {
        return "search";
    }
}
