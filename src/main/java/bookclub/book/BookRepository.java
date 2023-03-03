package bookclub.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

//    public Book createBook(Book book){
//        return (Book) HibernateUtils.addToDb(book);
//    }
//
//    public List <Book> getBooks() {
//        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
//            return session.createQuery("from Book", Book.class).list();
//        }
//    }

}
