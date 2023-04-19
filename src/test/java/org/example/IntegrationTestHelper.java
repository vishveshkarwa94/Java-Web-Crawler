package org.example;

import java.util.HashMap;
import java.util.Map;

public class IntegrationTestHelper {
    static Map<String, String> htmlPageMap = new HashMap<>();
    static Map<String, Integer> numOfChildURLsForPage = new HashMap<>();

    static {
        String p1 = "<HTML><HEAD></HEAD>" +
                "<BODY>" +
                "<a href = \"http://localhost:8080/page2\">page2</a>" +
                "<a href = \"http://localhost:8080/page3\">page3</a>" +
                "<a href = \"www.abc.com/page4\">page4</a>" +
                "</BODY>\n" +
                "</HTML>";
        String p2 = "<HTML><HEAD></HEAD>" +
                "<BODY>" +
                "<a href = \"http://localhost:8080/page4\">page4</a>" +
                "<a href = \"http://localhost:8080/page5\">page5</a>" +
                "</BODY>\n" +
                "</HTML>";
        String p3 = "<HTML><HEAD></HEAD>" +
                "<BODY>" +
                "<a href = \"http://localhost:8080/page6\">page6</a>" +
                "<a href = \"/page4\">page4</a>" +
                "</BODY>\n" +
                "</HTML>";
        String p4 = "<HTML><HEAD></HEAD>" +
                "<BODY>" +
                "<a href = \"http://localhost:8080/page8\">page8</a>" +
                "<a href = \"http://localhost:8080/page7\">page7</a>" +
                "<a href = \"http://localhost:8080/page1\">page1</a>" +
                "<a href = \"http://localhost:8080/page2\">page2</a>" +
                "</BODY>\n" +
                "</HTML>";
        String p5 = "<HTML><HEAD></HEAD>" +
                "<BODY>" +
                "<a href = \"http://localhost:8080/page2\">page2</a>" +
                "<a href = \"http://localhost:8080/page4\">page4</a>" +
                "</BODY>\n" +
                "</HTML>";
        String p6 = "<HTML><HEAD></HEAD>" +
                "<BODY>" +
                "<a href = \"http://localhost:8080/page3\">page2</a>" +
                "<a href = \"http://localhost:8080/page5\">page5</a>" +
                "<a href = \"http://localhost:8080/page1\">page1</a>" +
                "<a href = \"dummy/page4\">page4</a>" +
                "</BODY>\n" +
                "</HTML>";
        String p7 = "<HTML><HEAD></HEAD>" +
                "<BODY>" +
                "<a href = \"http://localhost:8080/page1\">page1</a>" +
                "</BODY>\n" +
                "</HTML>";
        String p8 = "<HTML><HEAD></HEAD>" +
                "<BODY>" +
                "<a href = \"http://localhost:8080/page3\">page3</a>" +
                "<a href = \"http://localhost:8080/page4\">page4</a>" +
                "</BODY>\n" +
                "</HTML>";

        htmlPageMap.put("page1",p1);
        htmlPageMap.put("page2",p2);
        htmlPageMap.put("page3",p3);
        htmlPageMap.put("page4",p4);
        htmlPageMap.put("page5",p5);
        htmlPageMap.put("page6",p6);
        htmlPageMap.put("page7",p7);
        htmlPageMap.put("page8",p8);
        numOfChildURLsForPage.put("http://localhost:8080/page1",2);
        numOfChildURLsForPage.put("http://localhost:8080/page2",2);
        numOfChildURLsForPage.put("http://localhost:8080/page3",1);
        numOfChildURLsForPage.put("http://localhost:8080/page4",4);
        numOfChildURLsForPage.put("http://localhost:8080/page5",2);
        numOfChildURLsForPage.put("http://localhost:8080/page6",3);
        numOfChildURLsForPage.put("http://localhost:8080/page7",1);
        numOfChildURLsForPage.put("http://localhost:8080/page8",2);

    }


}
