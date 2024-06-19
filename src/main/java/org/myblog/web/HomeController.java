package org.myblog.web;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String root(){
        return "redirect:/myblog";
    }

    @GetMapping("/myblog")
    public String home(){
        return "home";
    }
}
