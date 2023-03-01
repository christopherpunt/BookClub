package book;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import DatabaseUtils.HibernateUtils;
import org.hibernate.Session;

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
