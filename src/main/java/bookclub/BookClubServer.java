package bookclub;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class BookClubServer {
    public static void main(String[] args){
        SpringApplication.run(BookClubServer.class, args);
//        new BookClubServer(args);
    }

    public BookClubServer(String[] args) {



//        BookDao bookDao = new BookDao();
//
////        bookDao.addBook(new Book(9, 123, "Harry Potter 8", 12345, "a nice harry potter book", null, null));
//
//        System.out.println("Books:");
//
//        for (var book : bookDao.getBooks()) {
//            System.out.println(book);
//        }
    }
}
