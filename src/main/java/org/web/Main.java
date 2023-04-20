package org.web;


import static org.web.Util.ThreadManagementService.initializeExecutionService;

public class Main {

    /**
     * Main Function
     * @param args : Array containing seed URL (required) and max depth of web crawl.
     */
    public static void main(String[] args){
        if(args.length  == 0){
            System.out.println("ERROR: Please provide the seed URL for web crawl.");
            return;
        }
        String startURL = args[0];
        int maxDepth;
        if(args.length > 1) {
            String maxPagesToCrawl = args[1];
            try{
                maxDepth = Integer.parseInt(maxPagesToCrawl);
                if(maxDepth < 0) throw new NumberFormatException();
            }
            catch (NumberFormatException e){
                maxDepth  = 1000;
            }
        }
        else {
            maxDepth = 1000;
        }

        System.out.println("Starting web crawling with seed URL : "+startURL);
        initializeExecutionService(maxDepth, startURL);

    }

}