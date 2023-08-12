package unitTest;

import bookclub.purge.PurgingService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

public class PurgingServiceTest extends BaseUnitTest {

    @InjectMocks
    PurgingService purgingService;

    @Test
    public void testPurgingService(){
        purgingService.purgeEntities();
    }
}
