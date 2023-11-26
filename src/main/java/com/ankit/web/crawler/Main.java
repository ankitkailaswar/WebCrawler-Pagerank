package com.ankit.web.crawler;

import com.ankit.web.crawler.data.Configuration;
import com.ankit.web.crawler.data.Constants;
import com.ankit.web.crawler.data.Link;
import com.ankit.web.crawler.data.SharedData;
import com.ankit.web.crawler.executors.*;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

/**
 * The type Main.
 */
@Slf4j
public class Main {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        if (args.length == 2) {
            run(args);
        } else {
            log.error("Exactly two argument should be provided as imput param e.g. http://en.wikipedia.com 3");
        }
        return;
    }

    /**
     * Runs the application.
     *
     * @param args the args
     */
    public static void run(String[] args) {
        log.info("Starting application.");

        /**
         * TODO convert this to commandlin argument parser class
         */
        String confFileName = "config.ini";
        String confFile = Paths.get("src","main","resources", confFileName).toFile().getAbsolutePath();
        String env = "dev";
        List<String> urls = new ArrayList<>();
        urls.add(args[0]);
        Integer depth = Integer.parseInt(args[1]);

        try {

            log.debug("initialising shared resources among services.");
            // initialise common data objects
            Configuration conf = new Configuration(confFile, env, urls);
            SharedData data = new SharedData(conf.getTotalExpectedLinks(), conf.getBloomFilterProbability(), conf.getQueueSize(), urls, depth);
            Output output = Outputter.getOutputType(conf);

            // service initialisation
            log.debug("initialising services.");
            Service persistData = new PersistService(data, conf, output);
            persistData.start();

            Service webCrawler = new WebCrawler(data, conf);
            webCrawler.start();

            int timeIdleState = 0;
            int sleepTime = Constants.APP_POLL_WAIT_TIME;
            int idleTimeout = Constants.APP_IDLE_TIMEOUT_IN_SECS;

            while (true) {
                int numIdleWorkers = data.getIdleURLFetcherThreadsPool().size();
                if (numIdleWorkers == conf.getNumWorkers()) {
                    timeIdleState++;
                    log.info(String.format("Application check, total worker/s : %s idle worker/s : %s, time since all workers are idle : %s",
                            conf.getNumWorkers(), numIdleWorkers, timeIdleState * sleepTime));
                } else {
                    timeIdleState = 0;
                }
                if (timeIdleState * sleepTime > idleTimeout) {
                    /**
                     * TODO : this should be done by persist service, need to send SIG_TERM to persist service to drain
                     * remaining result from que and persist before termination.
                     */
                    storeRestofTheData(data, conf, output);
                    log.info(String.format("Exiting main application, since all threads are idle for last %s secs.", timeIdleState * sleepTime));
                    log.info("Application completed successfully.");
                    System.exit(0);
                }
                try {
                    Thread.sleep(sleepTime * 1000);
                } catch (InterruptedException e) {
                    log.error(String.format("Sleep interuppted in Main application. Error : ", e.toString()));
                }
            }
        } catch (IOException e) {
            log.error(String.format("Failed to run application, Error : ", e.toString()));
        }
    }

    /**
     * Store restof the data.
     * NOTE : remove this function once safe exit is implemented for main application.
     * @param data   the data
     * @param conf   the conf
     * @param output the output
     */
    public static void storeRestofTheData(SharedData data, Configuration conf, Output output) {
        List<Pair<Integer, Link>> chunk = null;
        synchronized (data.getProcessedLink()) {
            chunk = new ArrayList<Pair<Integer, Link>>(data.getProcessedLink().subList(0, conf.getMaxResultSize() - 1));
            data.getProcessedLink().subList(0, conf.getMaxResultSize() - 1).clear();

        }

        List<Triplet<String, String, String>> toBeSerialisedData = new ArrayList<>();
        for (Pair<Integer, Link> link : chunk) {
            Pair<String, String> linkData = link.getValue1().getLink();
            Integer level = data.getInitialDepth() - link.getValue0() + 1;
            toBeSerialisedData.add(new Triplet<>(level.toString(), linkData.getValue0(), linkData.getValue1()));
        }
        output.persist(toBeSerialisedData);
    }
}
