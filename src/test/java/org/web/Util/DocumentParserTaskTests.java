package org.web.Util;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.mockserver.model.*;
import org.mockserver.integration.ClientAndServer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class DocumentParserTaskTests {

    private static String p1 = "<HTML><HEAD></HEAD>" +
            "<BODY>" +
            "<a href = \"http://www.abc.com/page2\">page2</a>" +
            "<a href = \"http://www.abc.com/page3\">page3</a>" +
            "<a href = \"www.abc.com/page4\">page4</a>" +
            "</BODY>\n" +
            "</HTML>";
    static ClientAndServer clientServer;

    @BeforeAll
    static void setUp(){
        clientServer = ClientAndServer.startClientAndServer(8080);
        clientServer.when(new HttpRequest().withMethod("GET"))
                .respond(new HttpResponse().withStatusCode(HttpStatusCode.OK_200.code())
                        .withBody(p1));
    }

    @AfterAll
    static void shutDown(){
        clientServer.stop();
    }

    @Test
    public void searchForURLTest(){

        // Functions returns all the valid URLs
        DocumentParserTask task = new DocumentParserTask("http://localhost:8080",1,2);
        List<String> urls = task.searchForURL();
        assertEquals(2, urls.size());

        // Function returns null for in valid URL
        task = new DocumentParserTask("dummy:8080",1,1);
        urls = task.searchForURL();
        assertNull(urls);
    }


    @Test
    public void runTest(){

        PrintStream standardOut = System.out;
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Verify task is submitted for each valid URL found on page.
        try(MockedStatic<ThreadManagementService> mockedStatic = Mockito.mockStatic(ThreadManagementService.class)){
            final int[] count = {0};
            mockedStatic.when(()->ThreadManagementService.submitTask(anyString(),anyInt())).then((Answer<Void>) invocationOnMock -> {
                count[0]++;
                return null;
            });
            DocumentParserTask task = new DocumentParserTask("http://localhost:8080",1,2);
            task.run();
            assertEquals(2, count[0]);
        }
        System.setOut(standardOut);
    }

}
