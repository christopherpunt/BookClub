package bookclub.friend;

import bookclub.book.Book;
import bookclub.book.BookService;
import bookclub.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
public class FriendBooksController {
    @Autowired
    BookService bookService;

    @Autowired
    FriendService friendService;

    @Autowired
    NotificationService notificationService;

    @GetMapping("/searchFriendsBooks")
    public ModelAndView showSearchFriendsBooks(Principal principal){
        List<Book> friendsBooks = friendService.findAllFriendsBooks(principal.getName());
        ModelAndView modelAndView = new ModelAndView("friend/search-friends-books.html");
        modelAndView.addObject("friendsBooks", friendsBooks);
        return modelAndView;
    }

    @PostMapping("/searchFriendsBooks")
    public ModelAndView searchFriendsBooks(@RequestParam String searchTerm, Principal principal){
        List<Book> friendsBooks = friendService.findAllFriendsBooksMatchSearch(principal.getName(), searchTerm);
        ModelAndView modelAndView = new ModelAndView("friend/search-friends-books.html");
        modelAndView.addObject("friendsBooks", friendsBooks);
        return modelAndView;
    }

    @PostMapping("/borrowBook")
    public String borrowBook(@RequestParam Long lenderId, @RequestParam Long bookId, Principal principal){
        boolean returnValue = notificationService.sendBorrowRequest(principal.getName(), lenderId, bookId);

        if (!returnValue){
            return "There was a problem sending the borrow request";
        }

        return "redirect:/home";
    }

    @PostMapping("book/returnBook")
    public String returnBook(@RequestParam Long bookId, @RequestParam Long borrowedFromUserId){
        bookService.returnBook(bookId, borrowedFromUserId);
        return "redirect:/home";
    }

}
