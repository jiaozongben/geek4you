package com.gk4u.rss.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")

public class testapi {
    @GetMapping("/ok")
    public String findOrganizationById(){
        String ok = "ok";
        return ok;
    }
}
