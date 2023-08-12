package bookclub.purge;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class PurgingService {

    public void purgeEntities() {
        List<Class<?>> purgeableEntityClasses = getClassesWithAnnotation();

        for (Class<?> entityClass : purgeableEntityClasses) {
            PurgeableEntity purgeableAnnotation = entityClass.getAnnotation(PurgeableEntity.class);
            int daysBeforePurge = purgeableAnnotation.daysBeforePurge();
            Date thresholdDate = calculateThresholdDate(daysBeforePurge);

//            List<?> entitiesToPurge = entityManager.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e.status = :status AND e.modifiedDate < :thresholdDate")
//                    .setParameter("status", StatusEnum.DELETED) // Adjust as needed
//                    .setParameter("thresholdDate", thresholdDate)
//                    .getResultList();
//
//            // Delete the purged entities
//            for (Object entity : entitiesToPurge) {
//                entityManager.remove(entity);
//            }
        }
    }

    private Date calculateThresholdDate(int daysBeforePurge) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -daysBeforePurge);
        return calendar.getTime();
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
