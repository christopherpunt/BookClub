package bookclub.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    BookRepository bookDao;

    public void getBookFromGoogle(String isbn){
//        var client = HttpClient.newHttpClient();
//
//        var request = HttpRequest.newBuilder(URI.create("https://www.googleapis.com/books/v1/volumes?q=9780606323451"))
//                .header("accept", "application/json")
//                .build();
//        String responsestr = "";
//        var response = client.send(request, responsestr);

//        System.out.println(response.body());
    }

    public Book createBook(Book book){
        return bookDao.save(book);
    }

    public List<Book> getBooks(){
        return bookDao.findAll();
    }
}
