package org.example.Util;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.example.Util.ThreadManagementService.initializeExecutionService;
import static org.example.Util.ThreadManagementService.submitTask;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    public void initializeExecutionServiceTest() throws ReflectiveOperationException {

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
        assertEquals("",outputStreamCaptor.toString().trim());
        outputStreamCaptor.reset();
        assertNotNull(ThreadManagementService.executorService);
        assertEquals(1, ThreadManagementService.seenURLS.size());
        assertEquals(1, ThreadManagementService.numTasks);
        outputStreamCaptor.reset();

        // Try to initialize service twice.
        initializeExecutionService(1,"http://www.rescale.com/");
        assertEquals("WARNING: Execution service already initialized.",outputStreamCaptor.toString().trim());


    }

    @Test
    public void submitTaskTest() throws ReflectiveOperationException {

        setUpTest();

        // Submit task without initializing Execution Service.
        submitTask("dummy");
        assertEquals("WARNING: Execution Service not initialized.",outputStreamCaptor.toString().trim());
        outputStreamCaptor.reset();

        // Task successfully submitted after initializing service.
        initializeExecutionService(2, "http://www.rescale.com/");
        assertEquals(1,ThreadManagementService.numTasks);
        submitTask("dummy");
        assertEquals(2,ThreadManagementService.numTasks);
        assertEquals("",outputStreamCaptor.toString().trim());
        outputStreamCaptor.reset();

        // 3rd task is rejected
        submitTask("dummy");
        assertEquals(2,ThreadManagementService.numTasks);

    }

    private void setUpTest(){
        ThreadManagementService.executorService = null;
        ThreadManagementService.seenURLS = null;
        ThreadManagementService.numTasks = 0;
        ThreadManagementService.maxPagesToCrawl = 0;
        outputStreamCaptor.reset();
    }
}
