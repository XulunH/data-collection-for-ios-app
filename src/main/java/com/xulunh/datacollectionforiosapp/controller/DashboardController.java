package com.xulunh.datacollectionforiosapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/")
    public String dashboard() {
        return "redirect:/dashboard.html";
    }

    @GetMapping("/dashboard")
    public String dashboardPage() {
        return "redirect:/dashboard.html";
    }
}

