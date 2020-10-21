package com.zlimbo.bcweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("")
public class ChainControl {

    @RequestMapping(value = {"", "index"})
    @ResponseBody
    public ModelAndView chainInfo() {
        System.out.println("----------------------------------chainInfo ok");
        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;
    }
}
