package bookclub.controllers;

import bookclub.models.Book;
import bookclub.models.User;
import bookclub.repositories.BookRepository;
import bookclub.repositories.UserRepository;
import bookclub.services.BookService;
import bookclub.services.FriendService;
import bookclub.services.GoogleBookDetailsService;
import bookclub.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class BookController {
    @Autowired
    BookService bookService;
    
    @Autowired
    BookRepository bookDao;

    @Autowired
    UserRepository userDao;

    @Autowired
    FriendService friendService;

    @Autowired
    NotificationService notificationService;

    @GetMapping("/searchFriendsBooks")
    public String showSearchFriendsBooks(){
        return "search-friends-books";
    }

    @PostMapping("/searchFriendsBooks")
    public ModelAndView searchFriendsBooks(@RequestParam String searchTerm, Principal principal){
        List<Book> friendsBooks = friendService.findAllFriendsBooksMatchSearch(principal.getName(), searchTerm);
        ModelAndView modelAndView = new ModelAndView("search-friends-books.html");
        modelAndView.addObject("friendsBooks", friendsBooks);
        return modelAndView;
    }

    @GetMapping("/searchGoogleBooks")
    public String showCreateBookForm(){
        return "search-google-books";
    }

    @PostMapping("/searchGoogleBooks")
    public ModelAndView searchBookFromTitle(@RequestBody String title, Principal principal){
        List<Book> books = GoogleBookDetailsService.getBooksBasedOnTitle(title);

        ModelAndView modelAndView = new ModelAndView("search-google-books.html");
        modelAndView.addObject("books", books);
        return modelAndView;
    }

    @PostMapping(value = "/addBook", consumes = "application/json")
    public ResponseEntity<String> addBook(@RequestBody Book book, Principal principal) {
        boolean success = bookService.addBookForUser(principal.getName(), book);

        if (success){
            return ResponseEntity.ok("Book added to library successfully");
        }
        return ResponseEntity.badRequest().body("Failed to Add book to library");
    }

    @GetMapping("/book_details/{id}")
    public String getBookDetails(@PathVariable Long id, Model model, Principal principal){
        Optional<Book> book = bookDao.findById(id);
        Optional<User> userOptional = userDao.findByEmail(principal.getName());
        if (userOptional.isPresent()){
            List<User> friends = friendService.findAllFriendsFromUser(userOptional.get());

            if(book.isPresent()){
                model.addAttribute("book", book.get());
                model.addAttribute("friends", friends);
                return "book_details";
            }
        }

        return "no book found";
    }

    @GetMapping("/editBookDetails/{id}")
    public String editBookDetails(@PathVariable Long id, Model model){
        Optional<Book> book = bookDao.findById(id);

        if(book.isPresent()){
            model.addAttribute("book", book.get());
            return "edit_book_details";
        }
        return "no book found";
    }

    @PostMapping("/deleteBook/{id}")
    public RedirectView removeBook(@PathVariable Long id, Model model){
        bookService.deleteBook(id);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/home");
        return redirectView;
    }

    @PostMapping("/updateBook")
    public String updateBook(@ModelAttribute("book") Book book) {
        boolean updated = bookService.updateAllDetails(book);

        if (updated){
            return "redirect:/book_details/" + book.getId();
        }
        return "redirect:/home";
    }

    @PostMapping("/borrowBook")
    public ResponseEntity<String> borrowBook(@RequestParam Long friendId, @RequestParam Long bookId, Principal principal){
        boolean returnStatus = notificationService.sendBorrowRequest(principal.getName(), friendId, bookId);

        if (returnStatus){
            return ResponseEntity.ok("Book request sent");
        }
        return ResponseEntity.badRequest().body("there was a problem sending the borrow request");
    }

    @PostMapping("book/returnBook")
    public ResponseEntity<String> returnBook(@RequestParam Long bookId, @RequestParam Long borrowedFromUserId){
        bookService.returnBook(bookId, borrowedFromUserId);
        return ResponseEntity.ok("Book Returned!");
    }
}
