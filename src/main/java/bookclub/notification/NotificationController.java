package bookclub.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @Autowired
    NotificationCompletionService notificationCompletionService;

    @GetMapping("/notifications")
    public String getNotifications(Model model, Principal principal){
        List<Notification> notifications = notificationService.getNotificationsForUsername(principal.getName());
        model.addAttribute("notifications", notifications);
        return "notificationsPage";
    }

    @PostMapping("/notifications/confirm")
    public String confirmNotification(@RequestParam Long id, Principal principal){
        notificationCompletionService.completeNotification(id);
        return "redirect:/notifications";
    }
}
