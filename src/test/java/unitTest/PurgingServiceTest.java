package unitTest;

import bookclub.book.BookRepository;
import bookclub.purge.PurgingService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

public class PurgingServiceTest extends BaseUnitTest {

    @Mock
    BookRepository bookRepository;

    @Mock
    ApplicationContext applicationContext;

    @InjectMocks
    PurgingService purgingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void testPurgeEntities() {
//
//        ArgumentCaptor<String> beanCaptor = ArgumentCaptor.forClass(String.class);
//        when(applicationContext.getBean(beanCaptor.capture())).thenReturn(bookRepository);
//
//        List<Book> books = new ArrayList<>();
//
//        Book book = new Book();
//        book.setTitle("hello");
//        book.setModifiedDate(LocalDateTime.now().minusDays(5));
//        book.setStatus(StatusEnum.DELETED);
//
//        books.add(book);
//
//        ArgumentCaptor<LocalDateTime> dateCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
//        ArgumentCaptor<StatusEnum> statusCaptor = ArgumentCaptor.forClass(StatusEnum.class);
//
//        when(bookRepository.findByStatusAndModifiedDateBefore(statusCaptor.capture(), dateCaptor.capture())).thenReturn(books);
//
//        purgingService.purgeEntities();
//    }
//
//    @Test
//    public void testPurgeEntities2() {
//        Class<?> entityClass = Book.class;
//        OngoingStubbing<?> ongoingStubbing = lenient()
//                .when(applicationContext.getBean(entityClass.getAnnotation(PurgeableEntity.class).repositoryClass()));
//
//        ongoingStubbing.thenReturn(bookRepository);
//
//        when(bookRepository.findByStatusAndModifiedDateBefore(Mockito.any(StatusEnum.class), Mockito.any(LocalDateTime.class)))
//                .thenReturn(Collections.emptyList());
//
//        // Call the method to test
//        purgingService.purgeEntities();
//    }
}
