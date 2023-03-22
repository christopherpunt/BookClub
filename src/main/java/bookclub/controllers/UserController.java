package bookclub.controllers;

import bookclub.models.User;
import bookclub.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
//@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/user", method= RequestMethod.POST)
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @RequestMapping(value = "/users", method=RequestMethod.GET)
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @RequestMapping("/login")
    public String login(Model model){
        model.addAttribute("user", new User());
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        model.addAttribute("user", new User());

        return "signup_form";
    }

    @PostMapping("/register")
    public String processRegister(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userService.createUser(user);

        return "register_success";
    }

    @GetMapping("/currentUser")
    public String getCurrentUser(Principal principal){
        String name = principal.getName();
        return "hello" + name;
    }

}
