package org.web;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.HttpStatusCode;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.web.IntegrationTestHelper.htmlPageMap;
import static org.web.IntegrationTestHelper.numOfChildURLsForPage;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntegrationTest {

    static ClientAndServer clientServer;

    @BeforeAll
    static void setUp(){
        clientServer = ClientAndServer.startClientAndServer(8080);
        clientServer.when(new HttpRequest().withMethod("GET").withPath("/page1"))
                .respond(new HttpResponse().withStatusCode(HttpStatusCode.OK_200.code())
                        .withBody(htmlPageMap.get("page1")));
        clientServer.when(new HttpRequest().withMethod("GET").withPath("/page2"))
                .respond(new HttpResponse().withStatusCode(HttpStatusCode.OK_200.code())
                        .withBody(htmlPageMap.get("page2")));
        clientServer.when(new HttpRequest().withMethod("GET").withPath("/page3"))
                .respond(new HttpResponse().withStatusCode(HttpStatusCode.OK_200.code())
                        .withBody(htmlPageMap.get("page3")));
        clientServer.when(new HttpRequest().withMethod("GET").withPath("/page4"))
                .respond(new HttpResponse().withStatusCode(HttpStatusCode.OK_200.code())
                        .withBody(htmlPageMap.get("page4")));
        clientServer.when(new HttpRequest().withMethod("GET").withPath("/page5"))
                .respond(new HttpResponse().withStatusCode(HttpStatusCode.OK_200.code())
                        .withBody(htmlPageMap.get("page5")));
        clientServer.when(new HttpRequest().withMethod("GET").withPath("/page6"))
                .respond(new HttpResponse().withStatusCode(HttpStatusCode.OK_200.code())
                        .withBody(htmlPageMap.get("page6")));
        clientServer.when(new HttpRequest().withMethod("GET").withPath("/page7"))
                .respond(new HttpResponse().withStatusCode(HttpStatusCode.OK_200.code())
                        .withBody(htmlPageMap.get("page7")));
        clientServer.when(new HttpRequest().withMethod("GET").withPath("/page8"))
                .respond(new HttpResponse().withStatusCode(HttpStatusCode.OK_200.code())
                        .withBody(htmlPageMap.get("page8")));
    }

    @AfterAll
    static void shutDown(){
        clientServer.stop();
    }

    @Test
    public void test() throws InterruptedException {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        System.out.println("Starting Integration Test");
        // Seed URL to start
        final String seedURL = "http://localhost:8080/page1";
        // Capture standard out
        PrintStream standardOut = System.out;
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        // Arguments for main method
        String[] args = new String[]{seedURL,"3"};

        // Begin test
        Main.main(args);
        System.setOut(standardOut);

        String out = outputStreamCaptor.toString();
        String header = out.substring(0,out.indexOf('\n'));
        // Validate first statement
        assertEquals("Starting web crawling with seed URL : "+seedURL, header.trim());

        // Get results from crawl
        String crawlResult = out.substring(out.indexOf('\n')+1);
        String crawls[] = crawlResult.split("\n\r\n");

        // Validate 8 Pages are visited
        assertEquals(6, crawls.length);

        // Validate number of child URLs for parent URL
        for(String crawl : crawls){
            String parentURL = crawl.substring(0, crawl.indexOf('\n'));
            String childURLs = crawl.substring(crawl.indexOf('\n')+1);
            String[] childURLArray = childURLs.split("\n\t");
            assertEquals(numOfChildURLsForPage.get(parentURL), childURLArray.length);
        }

        final String ANSI_GREEN = "\u001B[32m";
        final String ANSI_RESET = "\u001B[0m";

        System.out.println("Integration test complete. Following output generated.\n");
        System.out.println(ANSI_GREEN+out.trim()+ANSI_RESET);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

    }

}
