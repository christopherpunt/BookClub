package bookclub.controllers;

import bookclub.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserRepository userRepo;

    @RequestMapping(value = "/users", method= RequestMethod.GET)
    public String getUsers(Model model){
        model.addAttribute("users", userRepo.findAll());
        return "admin/user-management";
    }
}
