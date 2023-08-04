package bookclub.friend;

import bookclub.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
public class FriendController {

    @Autowired
    private FriendService friendService;

    @GetMapping("/myFriends")
    public ModelAndView getFriendsList(Principal principal){
        List<User> friends = friendService.findAllFriendsFromUser(principal.getName());
        ModelAndView modelAndView = new ModelAndView("friend/friends.html");
        modelAndView.addObject("friends", friends);
        return modelAndView;
    }

    @GetMapping("/addFriend")
    public String addFriendView(){
        return "friend/add-friend";
    }

    @PostMapping("/addFriend")
    public String addFriend(@ModelAttribute User newFriend, Principal principal){
        friendService.addNewFriendship(principal.getName(), newFriend);

        return "redirect:/myFriends";
    }
}
