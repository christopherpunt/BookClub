package bookclub.shared;

import bookclub.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class DefaultController {

    @Autowired
    NotificationService notificationService;

    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        // Add common data to the model
        if (principal != null){
            model.addAttribute("username", principal.getName());
            model.addAttribute("notificationCount", notificationService.getUncompletedNotificationsForUser(principal.getName()).size());
        }
    }
}
