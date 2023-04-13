package bookclub.controllers;

import bookclub.models.Book;
import bookclub.services.BookService;
import bookclub.services.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/searchFriendsBooks")
    public ModelAndView showSearchFriendsBooks(Principal principal){
        List<Book> friendsBooks = friendService.findAllFriendsBooks(principal.getName());
        ModelAndView modelAndView = new ModelAndView("search-friends-books.html");
        modelAndView.addObject("friendsBooks", friendsBooks);
        return modelAndView;
    }

    @PostMapping("/searchFriendsBooks")
    public ModelAndView searchFriendsBooks(@RequestParam String searchTerm, Principal principal){
        List<Book> friendsBooks = friendService.findAllFriendsBooksMatchSearch(principal.getName(), searchTerm);
        ModelAndView modelAndView = new ModelAndView("search-friends-books.html");
        modelAndView.addObject("friendsBooks", friendsBooks);
        return modelAndView;
    }

    @PostMapping("/borrowBook")
    public ResponseEntity<String> borrowBook(@RequestParam Long lenderId, @RequestParam Long bookId, Principal principal){
        boolean returnValue = bookService.borrowBook(principal.getName(), lenderId, bookId);

        if (!returnValue){
            return ResponseEntity.badRequest().body("there was a problem sending the borrow request");
        }

        return ResponseEntity.ok("Book request sent");
    }

    @PostMapping("book/returnBook")
    public ResponseEntity<String> returnBook(@RequestParam Long bookId, @RequestParam Long borrowedFromUserId){
        bookService.returnBook(bookId, borrowedFromUserId);
        return ResponseEntity.ok("Book Returned!");
    }

}
