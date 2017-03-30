package com.fzc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.InetAddress;

/**
 * Created by mark on 17-3-29.
 */

@Controller
public class MainController {


    @RequestMapping("/index")
    public String homePage(Model model) throws Exception {


        model.addAttribute("ipAddress", InetAddress.getLocalHost().getHostName() + ":9876");

        model.addAttribute("key", "value");
        return "index";
    }
}
