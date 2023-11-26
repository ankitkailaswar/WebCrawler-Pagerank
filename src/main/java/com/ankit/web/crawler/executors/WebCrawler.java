package com.ankit.web.crawler.executors;

import com.ankit.web.crawler.data.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Service implementation for crawling
 * http links
 */
@Slf4j
public class WebCrawler extends Service {

    /**
     * Instantiates a new Web crawler.
     *
     * @param data the data
     * @param conf the conf
     */
    public WebCrawler(SharedData data, Configuration conf) {
        super(data, conf);
    }

    public void run() {
        log.info("Starting WebCrawler service.");
        int numWorkers = conf.getNumWorkers();
        Fetcher[] workers = new Fetcher[numWorkers];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Fetcher(data, conf.getPageRankPolicy(), conf.getCrawlingPolicy(), i + 1);
            workers[i].start();
        }

        try {
            for (int i = 0; i < workers.length; i++) {
                workers[i].join();
            }
            log.info("Shutting down WebCrawler service successfully.");
        } catch (InterruptedException e) {
            log.error(String.format("Webcrawler service interuptted & terminated unexpectedly. ERROR : ", e.toString()));
        }
    }
}
