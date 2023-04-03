package bookclub.repositories;

import bookclub.models.Book;
import bookclub.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByUser(User user);

    List<Book> findByUserIdIn(List<Long> userIds);
}
