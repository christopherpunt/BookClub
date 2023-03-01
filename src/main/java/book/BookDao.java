package book;

import DatabaseUtils.HibernateUtils;
import org.hibernate.Session;

import java.util.List;

public class BookDao {

    public Book addBook(Book book){
        return (Book) HibernateUtils.addToDb(book);
    }

    public List <Book> getBooks() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Book", Book.class).list();
        }
    }


}
