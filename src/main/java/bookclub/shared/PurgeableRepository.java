package bookclub.shared;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.time.LocalDateTime;
import java.util.List;

@NoRepositoryBean
public interface PurgeableRepository<T> extends JpaRepository<T, Long> {
    List<T> findByStatusAndModifiedDateBefore(StatusEnum status, LocalDateTime modifiedDate);
}
