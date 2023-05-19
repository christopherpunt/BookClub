package bookclub.controllers;

import bookclub.enums.UserRoleEnum;
import bookclub.models.User;
import bookclub.models.UserRole;
import bookclub.repositories.UserRepository;
import bookclub.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepo;

    @RequestMapping(value = "/users", method= RequestMethod.GET)
    public String getUsers(Model model){
        model.addAttribute("users", userRepo.findAll());
        return "admin/user-management";
    }

    @RequestMapping("/users/{userId}/edit")
    public String showEditUserPage(@PathVariable("userId") Long userId, Model model) {
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isEmpty()) {
            return "redirect:/users";
        }
        User user = userOptional.get();
        var enums = Arrays.stream(UserRoleEnum.values()).map(UserRole::new).collect(Collectors.toList());
        model.addAttribute("user", user);
        model.addAttribute("userRoles", user.getUserRoles().stream().map(UserRole::getUserRole).toList());
        model.addAttribute("allRoles", enums);
        return "admin/adminEditUser";
    }

    @PostMapping("/users/edit")
    public String editUser(@ModelAttribute("user") @Validated User user,
                           @RequestParam("selectedRoles") List<String> selectedRoles,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", UserRoleEnum.values());
            return "admin/adminEditUser";
        }

        userService.updateUser(user, selectedRoles);

        return "redirect:/admin/users";
    }

}
