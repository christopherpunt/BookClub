package unitTest;

import bookclub.book.Book;
import bookclub.book.BookRepository;
import bookclub.user.EmailService;
import bookclub.user.User;
import bookclub.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import utils.BookTestUtils;
import utils.UserTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EmailServiceTest extends BaseUnitTest {

    @Mock
    BookRepository bookRepo;

    @Mock
    UserRepository userRepo;

    @Mock
    JavaMailSender mailSender;

    @InjectMocks
    EmailService emailService;

    @Test
    public void sendEmailTest(){
        //arrange
        User loaner = UserTestUtils.createUser("Chris", "Punt", "chrispunt13@icloud.com", null);
        User borrower = UserTestUtils.createUser("Sydney", "Punt", "christopherpunt@outlook.com", null);
        Book book = BookTestUtils.createOwnedBook(loaner);

        when(bookRepo.findById(book.getId())).thenReturn(Optional.of(book));
        when(userRepo.findByEmail(loaner.getEmail())).thenReturn(Optional.of(loaner));
        when(userRepo.findByEmail(borrower.getEmail())).thenReturn(Optional.of(borrower));

        //act
        emailService.sendBookRequestNotification(borrower.getEmail(), loaner.getEmail(), book.getId());

        //assert
        ArgumentCaptor<SimpleMailMessage> emailCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(mailSender).send(emailCaptor.capture());

        assertNotNull(emailCaptor.getValue().getTo());
        assertEquals(1, emailCaptor.getValue().getTo().length);
        assertEquals(loaner.getEmail(), emailCaptor.getValue().getTo()[0]);
        assertTrue(emailCaptor.getValue().getText().contains(borrower.getFullName()));
        assertTrue(emailCaptor.getValue().getText().contains(book.getTitle()));

    }
}
