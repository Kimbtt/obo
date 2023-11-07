package com.example.demo.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DashBoardController {
    @GetMapping("/admin")
    public String getAdminDashBoard(Model model){
        return "admin/dashboard";
    }
}
