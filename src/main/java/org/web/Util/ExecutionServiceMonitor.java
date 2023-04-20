package org.web.Util;

import java.time.Duration;
import java.time.Instant;

import static org.web.Util.ThreadManagementService.executorService;
import static org.web.Util.ThreadManagementService.lastTaskExecuted;

/**
 * The class monitors ThreadManagementService and shuts it down if ideal for more than 5 seconds.
 */
public class ExecutionServiceMonitor implements Runnable{

    /**
     * Run method.
     */
    @Override
    public void run() {
        final long fiveSeconds = 5000;
        while (!executorService.isShutdown()){
            if((Duration.between(lastTaskExecuted, Instant.now()).toMillis() > fiveSeconds)){
                // Shutdown if last task was submitted more than 5 seconds ago.
                executorService.shutdown();
            }
            try {
                // Check status every 5 seconds.
                Thread.sleep(fiveSeconds);
            } catch (InterruptedException e) {
                new ExecutionServiceMonitor().run();
            }
        }
    }
}
