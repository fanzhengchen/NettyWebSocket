package com.fzc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by mark on 17-3-29.
 */

@Controller
public class MainController {


    @RequestMapping("/index")
    public String homePage(){
        return "index";
    }
}
