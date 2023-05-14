package bookclub.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    @GetMapping("/testpage")
    public String testPage(){
        return "testPage";
    }
}
