package org.web.Util;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.web.Util.ThreadManagementService.initializeExecutionService;
import static org.web.Util.ThreadManagementService.submitTask;

@ExtendWith(MockitoExtension.class)
public class ThreadManagementServiceTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        outputStreamCaptor.reset();
    }

    @AfterEach
    public void shutDown() {
        System.setOut(standardOut);
    }

    /**
     * Test ThreadManagementService::initializeExecutionService function.
     */
    @Test
    public void initializeExecutionServiceTest() throws InterruptedException {

        setUpTest();
        // Check with invalid URL
        initializeExecutionService(1,"dummy");
        assertEquals("ERROR: Invalid URL provided.",outputStreamCaptor.toString().trim());
        outputStreamCaptor.reset();

        // Check for valid but unreachable URL
        initializeExecutionService(1,"http://www.cdfhjknldnfasdfasf.com/");
        assertEquals("ERROR: Cannot reach http://www.cdfhjknldnfasdfasf.com/",outputStreamCaptor.toString().trim());
        outputStreamCaptor.reset();

        // Check successful initialization
        initializeExecutionService(1,"http://www.rescale.com/");
        assertNotEquals("",outputStreamCaptor.toString().trim());
        outputStreamCaptor.reset();
        assertNotNull(ThreadManagementService.executorService);
        assertEquals(1, ThreadManagementService.seenURLS.size());
        assertNotNull(ThreadManagementService.lastTaskExecuted);
        outputStreamCaptor.reset();

        // Try to initialize service twice.
        initializeExecutionService(1,"http://www.rescale.com/");
        assertEquals("WARNING: Execution service already initialized.",outputStreamCaptor.toString().trim());

    }

    @Test
    public void submitTaskTest() throws InterruptedException {

        setUpTest();

        // Submit task without initializing Execution Service.
        submitTask("dummy", 1);
        assertEquals("WARNING: Execution Service not initialized.",outputStreamCaptor.toString().trim());
        outputStreamCaptor.reset();

        // Task successfully submitted after initializing service.
        initializeExecutionService(1, "http://www.google.com/");
        Instant a = ThreadManagementService.lastTaskExecuted;
        assertNotNull(a);
    }

    private void setUpTest(){
        ThreadManagementService.executorService = null;
        ThreadManagementService.seenURLS = null;
        ThreadManagementService.maxDepth = 0;
        ThreadManagementService.lastTaskExecuted = null;
        outputStreamCaptor.reset();
    }
}
