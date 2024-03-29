package bookclub.book;

import bookclub.shared.PurgeableRepository;
import bookclub.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends PurgeableRepository<Book> {
    List<Book> findByUser(User user);

    @Query("SELECT b FROM Book b WHERE b.user = :user AND b.Isbn = :isbn")
    Book findByUserAndIsbn(User user, String isbn);

    List<Book> findByUserIdIn(List<Long> userIds);
}
