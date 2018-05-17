package com.solbox.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ??? on 2018-05-17.
 */
@Controller
public class MainController {
    @RequestMapping("/")
    public String start() {
        return "redirect:/login";
    }

    @RequestMapping("/login")
    public String login(HttpServletRequest req, Model model) {
        String errCode = req.getParameter("error");
        if(errCode == null) {
            errCode = "";
        }
        model.addAttribute("errCode", errCode);
        return "login";
    }

    @RequestMapping("/main")
    public String main() { return "main"; }

    @RequestMapping("/streaming/dashboard")
    public String streamingDashboard() {
        return "streaming/dashboard";
    }

    @RequestMapping("/streaming/history")
    public String streamingHistory() {
        return "streaming/history";
    }

    @RequestMapping("/streaming/modalPlay")
    public String modalPlay() {
        return "streaming/modalPlay";
    }

}
