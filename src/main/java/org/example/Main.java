package org.example;


import org.example.Util.DocumentParserTask;
import org.example.Util.ThreadManagementService;

import static org.example.Util.ThreadManagementService.initializeExecutionService;

public class Main {

    /**
     * Main Function
     * @param args : Array containing seed URL (required) and max number of pages to visit.
     */
    public static void main(String[] args){
        if(args.length  == 0){
            System.out.println("ERROR: Please provide the seed URL for web crawl.");
            return;
        }
        String startURL = args[0];
        int maxPagesToCrawlNumber;
        if(args.length > 1) {
            String maxPagesToCrawl = args[1];
            try{
                maxPagesToCrawlNumber = Integer.parseInt(maxPagesToCrawl);
                if(maxPagesToCrawlNumber < 0) throw new NumberFormatException();
            }
            catch (NumberFormatException e){
                maxPagesToCrawlNumber  = Integer.MAX_VALUE;
            }
        }
        else {
            maxPagesToCrawlNumber = Integer.MAX_VALUE;
        }

        System.out.println("Starting web crawling with seed URL : "+startURL);
        initializeExecutionService(maxPagesToCrawlNumber, startURL);

    }

}