package com.zlimbo.bcweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("")
public class InvoiceInsert {

    @RequestMapping(value = "invoiceInsert")
    @ResponseBody
    public ModelAndView invoiceInsert() {
        System.out.println("----------------------------------invoiceInsert ok");
        ModelAndView modelAndView = new ModelAndView("invoiceInsert");
        return modelAndView;
    }

}
