package org.example.Util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ThreadManagementService {

    // Service to manage threads.
    static protected ExecutorService executorService;

    // Set to store visited URLS.
    static protected Set<String> seenURLS;

    // Variable to keep track of pages visited.
    static protected int numTasks;

    // variable to store max number of web pages to visit.
    static protected int maxPagesToCrawl;

    /**
     * Function to initialize the Execution Service and submit first URL to be crawled.
     * @param URL : Seed URL to being the web crawl.
     * @param max Max number of web pages to visit.
     */
    public static void initializeExecutionService(int max, String URL){
        if(executorService != null){
            System.out.println("WARNING: Execution service already initialized.");
        }
        if(!checkIfCanReachSeedURL(URL)) return;
        executorService = Executors.newCachedThreadPool();
        seenURLS = new HashSet<>();
        numTasks = 0;
        maxPagesToCrawl = max;
        submitTask(URL);
    }

    /**
     * Function to add tasks to task pool.
     * @param URL : URL of the web page that is to be searched for URLs.
     */

    public static void submitTask(String URL){

        if(executorService == null) {
            System.out.println("WARNING: Execution Service not initialized.");
            return;
        }
        try{
            if(executorService.isShutdown()) return;
            if(checkIfURLVisited(URL)) return;
            if(checkNumTasks()){
                executorService.shutdown();
                return;
            }
            addToSeenURLHashSet(URL);
            executorService.submit(new DocumentParserTask(URL));
            incrementNumTasks();
        }
        catch (Exception e) {
            System.out.println(e);
        }
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
     * Function to increment numTasks when new web page is visited.
     */
    private static synchronized void incrementNumTasks(){
        numTasks++;
    }

    /**
     * Function to check if maximum number of web page visit has been reached.
     * @return : If maximum number of web page visit has been reached.
     */
    private static synchronized boolean checkNumTasks(){
        return numTasks >= maxPagesToCrawl;
    }

    /**
     * Function to check if seed URL is valid and reachable.
     * @param URLString : Seed URL
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
