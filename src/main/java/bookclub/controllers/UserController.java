package bookclub.controllers;

import bookclub.enums.UserRoleEnum;
import bookclub.models.User;
import bookclub.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/registerAdminUser")
    @ResponseBody
    public String registerAsAdmin(Principal principal){
        userService.registerAsRole(principal.getName(), UserRoleEnum.ADMIN);
        return "Registration Successful";
    }

    @GetMapping("/registerUserRole")
    @ResponseBody
    public String registerAsUser(Principal principal){
        userService.registerAsRole(principal.getName(), UserRoleEnum.USER);
        return "Registration Successful";
    }

    @RequestMapping(value = "/user", method= RequestMethod.POST)
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }



    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        model.addAttribute("user", new User());

        return "signup_form";
    }

    @PostMapping("/register")
    public String processRegister(User user) {
        userService.createUser(user);

        return "register_success";
    }

    @GetMapping("/currentUser")
    public String getCurrentUser(Principal principal){
        String name = principal.getName();
        return "hello" + name;
    }
}
