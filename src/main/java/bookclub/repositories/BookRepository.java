package bookclub.repositories;

import bookclub.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    @Query(value = "SELECT * FROM book WHERE book.user_id = :userId", nativeQuery = true)
    List<Book> findAllBooksForUser(@Param("userId") int userId);
}
