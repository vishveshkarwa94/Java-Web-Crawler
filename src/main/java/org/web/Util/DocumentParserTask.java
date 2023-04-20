package org.web.Util;

import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.web.Util.ThreadManagementService.submitTask;


/**
 * Thread class that searches web page for URLs.
 */
public class DocumentParserTask implements Runnable{

    // URL of the page to be searched.
    private String URL;

    // Current depth of search.
    private int currentDepth;

    // Maximum depth of search.
    private int maxDepth;

    /**
     * Parameterized constructor.
     * @param URL : URL of the web page to be searched for URLs.
     * @param currentDepth : Current search depth.
     * @param maxDepth : Max search depth.
     */
    public DocumentParserTask(String URL, int currentDepth, int maxDepth) {
        this.URL = URL;
        this.currentDepth = currentDepth;
        this.maxDepth = maxDepth;
    }

    /**
     * Function to fetch HTML document and return URLs present on the page.
     * @return : List of URLs present on the page.
     */
    protected List<String> searchForURL(){
        try{
            List<String> urls = new ArrayList<>();
            Connection connection = Jsoup.connect(URL);
            Document htmlDocument = connection.get();
            Elements anchorTags = htmlDocument.getElementsByTag("a");
            anchorTags.forEach((tag) -> {
                String parsedURL = tag.attr("href");
                if(validURL(parsedURL)) urls.add(parsedURL);
            });
            return urls;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Function to validate URLs.
     * @param url URL to be checked
     * @return : If URL is valid
     */
    boolean validURL(String url){
        // For unit and integration test.
        if(url.startsWith("http://localhost:")) return true;
        UrlValidator validator = new UrlValidator();
        return validator.isValid(url);
    }

    /**
     * Thread's run function that fetches URLs on the page and create tasks for them.
     */
    @Override
    public void run(){
        if(!validURL(URL)) return;
        List<String> result = searchForURL();
        if(result == null) return;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(URL+"\n");
        for(String subURL : result){
            stringBuilder.append("\t"+subURL+"\n");
            // If depth not reached create tasks for sub URLs
            if(currentDepth < maxDepth) submitTask(subURL, currentDepth+1);
        }
        System.out.println(stringBuilder);
    }
}
