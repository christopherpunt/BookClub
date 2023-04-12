package bookclub.controllers;

import bookclub.models.User;
import bookclub.repositories.UserRepository;
import bookclub.services.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class FriendController {
    @Autowired
    private UserRepository userDao;

    @Autowired
    private FriendService friendService;

    @GetMapping("/myFriends")
    public ModelAndView getFriendsList(Principal principal){
        Optional<User> user = userDao.findByEmail(principal.getName());

        if (user.isEmpty()){
            System.out.println("FriendController: no user found with that email");
            return null;
        }

        //TODO: don't get user before sending to friend service
        List<User> friends = friendService.findAllFriendsFromUser(user.get());
        ModelAndView modelAndView = new ModelAndView("friends.html");
        modelAndView.addObject("friends", friends);
        return modelAndView;
    }

    @GetMapping("/addFriend")
    public String addFriendView(){
        return "add-friend";
    }

    @PostMapping("/addFriend")
    public String addFriend(@ModelAttribute User newFriend, Principal principal){
        friendService.addNewFriendship(principal.getName(), newFriend);

        return "redirect:/myFriends";
    }
}
