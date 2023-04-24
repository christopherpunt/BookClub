package bookclub.services;

import bookclub.models.Book;
import bookclub.models.User;
import bookclub.repositories.BookRepository;
import bookclub.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private BookRepository bookDao;

    @Autowired
    private UserRepository userDao;

    public void sendBookRequestNotification(String loanerEmail, String borrowerEmail, Long bookId){

        Optional<Book> bookOptional = bookDao.findById(bookId);
        Optional<User> loanerOptional = userDao.findByEmail(loanerEmail);
        Optional<User> borrowerOptional = userDao.findByEmail(borrowerEmail);


        if (bookOptional.isEmpty() || loanerOptional.isEmpty() || borrowerOptional.isEmpty()){
            return;
        }

        User loaner = loanerOptional.get();
        User borrower = borrowerOptional.get();
        Book book = bookOptional.get();

        SimpleMailMessage email = new SimpleMailMessage();

        email.setTo(loanerEmail);
        email.setSubject("Book Request Notification");
        String text = "<p>Dear " + loaner.getFirstName() + ",</p>"
                + "<p>Your friend " + borrower.getFullName() +" has requested to borrow the book: " + book.getTitle() + "</p>"
                + "<p>Thank you for using our app!</p>";
        email.setText(text);
        mailSender.send(email);
    }
}
