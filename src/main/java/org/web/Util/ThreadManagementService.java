package org.web.Util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class ThreadManagementService {

    // Service to manage threads.
    static protected ExecutorService executorService;

    // Set to store visited URLS.
    static protected Set<String> seenURLS;

    // Property to store max number of web pages to visit.
    static protected int maxDepth;

    // Property to store the time when last task was submitted to the execution service.
    static protected Instant lastTaskExecuted;


    /**
     * Function to initialize the Execution Service and submit first URL to be crawled.
     * @param URL : Seed URL to being the web crawl.
     * @param max Max number of web pages to visit.
     */
    public static void initializeExecutionService(int max, String URL){
        if(executorService != null){
            System.out.println("WARNING: Execution service already initialized.");
            return;
        }
        if(!checkIfCanReachSeedURL(URL)) return;
        executorService = Executors.newCachedThreadPool();
        seenURLS = new HashSet<>();
        maxDepth = max;
        submitTask(URL, 1);
        new ExecutionServiceMonitor().run();
    }

    /**
     * Function to add tasks to task pool.
     * @param URL : URL of the web page that is to be searched for URLs.
     */

    public static void submitTask(String URL, int currentDepth){

        if(executorService == null) {
            System.out.println("WARNING: Execution Service not initialized.");
            return;
        }
        try{
            if(executorService.isShutdown()) return;
            if(checkIfURLVisited(URL)) return;
            addToSeenURLHashSet(URL);
            updateLastExecutionTime();
            executorService.submit(new DocumentParserTask(URL, currentDepth, maxDepth));
        }
        catch (Exception e) {}
    }

    /**
     * Function to add url to seen list after striping 'www.' and trailing '/' if any.
     * @param URL : URL to be added to seen list.
     */
    private static synchronized void addToSeenURLHashSet(String URL){
        URL = URL.replaceFirst("www.","");
        if(URL.charAt(URL.length()-1) == '/') URL = URL.substring(0,URL.length()-1);
        seenURLS.add(URL);
    }

    /**
     * Function to check if URL visited.
     * @param URL : URL to be checked.
     * @return : If the URL was visited.
     */
    private static synchronized boolean checkIfURLVisited(String URL){
        URL = URL.replaceFirst("www.","");
        if(URL.charAt(URL.length()-1) == '/') URL = URL.substring(0,URL.length()-1);
        return seenURLS.contains(URL);
    }

    /**
     * Update last task execution time.
     */
    private static synchronized void updateLastExecutionTime(){
        lastTaskExecuted = Instant.now();
    }

    /**
     * Function to check if seed URL is valid and reachable.
     * @param URLString : Seed URL.
     * @return : If seed URL is valid and reachable.
     */
    private static boolean checkIfCanReachSeedURL(String URLString){
        try{
            URL url = new URL(URLString);
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();
            return true;
        }
        catch (MalformedURLException e){
            System.out.println("ERROR: Invalid URL provided.");
            return false;
        }
        catch(IOException e) {
            System.out.println("ERROR: Cannot reach "+URLString);
            return false;
        }
    }

}
