package bookclub.purge;

import bookclub.shared.BaseEntity;
import bookclub.shared.PurgeableRepository;
import bookclub.shared.StatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PurgingService {
    private Logger logger = LoggerFactory.getLogger(PurgingService.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Scheduled(cron = "0 */1 * * * *") // Schedule to run every 5 minutes
    public List<BaseEntity> purgeEntities() {
        List<BaseEntity> purgedEntities = new ArrayList<>();

        List<Class<?>> purgeableEntityClasses = getClassesWithAnnotation();

        for (Class<?> entityClass : purgeableEntityClasses) {
            PurgeableEntity purgeableAnnotation = entityClass.getAnnotation(PurgeableEntity.class);
            int daysBeforePurge = purgeableAnnotation.daysBeforePurge();
            LocalDateTime thresholdDate = calculateThresholdDate(daysBeforePurge);

            @SuppressWarnings("unchecked")
            PurgeableRepository<BaseEntity> repository = (PurgeableRepository<BaseEntity>)
                    applicationContext.getBean(purgeableAnnotation.repositoryClass());
            List<BaseEntity> entitiesToPurge = repository.findByStatusAndModifiedDateBefore(StatusEnum.DELETED, thresholdDate);

            // Delete the purged entities
            repository.deleteAll(entitiesToPurge);
            purgedEntities.addAll(entitiesToPurge);
        }

        logger.info("Successfully purged {} items from the database", purgedEntities.size());
        return purgedEntities;
    }


    private LocalDateTime calculateThresholdDate(int daysBeforePurge) {
        return LocalDateTime.now().minusDays(daysBeforePurge);
    }

    private List<Class<?>> getClassesWithAnnotation() {
        List<Class<?>> classesWithAnnotation = new ArrayList<>();

        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(PurgeableEntity.class));

        for (BeanDefinition beanDefinition : scanner.findCandidateComponents("bookclub")) {
            try {
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                classesWithAnnotation.add(clazz);
            } catch (ClassNotFoundException e) {
                // Handle class not found exception
            }
        }

        return classesWithAnnotation;
    }
}
