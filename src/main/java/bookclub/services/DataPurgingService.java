package bookclub.services;

import bookclub.annotations.Purgeable;
import bookclub.models.Book;
import bookclub.models.Friendship;
import bookclub.models.Notification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DataPurgingService {

    @Autowired
    private EntityManager entityManager;

    @Scheduled(cron = "0 0 0 * * ?") // executes at midnight every day
    public void scheduleDataPurging() {
        purgeData();
    }

    public void purgeData() {
        purgeTables(Book.class);
        purgeTables(Notification.class);
        purgeTables(Friendship.class);
    }

    private <T> void purgeTables(Class<T> clazz) {
        Purgeable purgeable = clazz.getAnnotation(Purgeable.class);
        if (purgeable != null) {
            int statusThreshold = purgeable.statusThreshold();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaDelete<T> delete = cb.createCriteriaDelete(clazz);
            Root<T> root = delete.from(clazz);
            delete.where(cb.greaterThan(root.get("status"), statusThreshold));
            entityManager.createQuery(delete).executeUpdate();
        }
    }
}