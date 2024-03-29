package bookclub.user;

import bookclub.book.Book;
import bookclub.book.BookRepository;
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
    private BookRepository bookRepo;

    @Autowired
    private UserRepository userRepo;

    public void sendBookRequestNotification(String borrowerEmail, String loanerEmail, Long bookId){

        Optional<Book> bookOptional = bookRepo.findById(bookId);
        Optional<User> loanerOptional = userRepo.findByEmail(loanerEmail);
        Optional<User> borrowerOptional = userRepo.findByEmail(borrowerEmail);


        if (bookOptional.isEmpty() || loanerOptional.isEmpty() || borrowerOptional.isEmpty()){
            return;
        }

        User loaner = loanerOptional.get();
        User borrower = borrowerOptional.get();
        Book book = bookOptional.get();

        SimpleMailMessage email = new SimpleMailMessage();

        email.setTo(loanerEmail);
        email.setSubject("Book Request Notification");
        String text = "Dear " + loaner.getFirstName() + ","
                + "\n\nYour friend " + borrower.getFullName() +" has requested to borrow the book: " + book.getTitle()
                + "\n\nThank you for using our app!";
        email.setText(text);
        mailSender.send(email);
    }

    public void sendFriendRequest(String senderEmail, String receiverEmail) {
        Optional<User> senderOptional = userRepo.findByEmail(senderEmail);
        Optional<User> receiverOptional = userRepo.findByEmail(receiverEmail);

        if (senderOptional.isEmpty() || receiverOptional.isEmpty()){
            return;
        }

        User sender = senderOptional.get();
        User receiver = receiverOptional.get();

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(receiverEmail);
        email.setSubject("ShareABook Friend Request");
        String text;
        if (receiver.isRegistered()){
            text = sender.getFullName() + " Invited you to be friends with them, " +
                    "go accept their friend request now to start borrowing books from them";
        } else{
            text = sender.getFullName() + " Invited you to join ShareABook to be friends with them. " +
                    "This is a platform where you can set up your personal library and view what books " +
                    "your friends have in their library and share your books with others. " +
                    "Join now at ShareABook.AzureWebsites.net";
        }
        email.setText(text);

        mailSender.send(email);
    }
}
