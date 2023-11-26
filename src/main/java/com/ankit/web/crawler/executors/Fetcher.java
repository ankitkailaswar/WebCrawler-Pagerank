package com.ankit.web.crawler.executors;

import com.ankit.web.crawler.data.Constants;
import com.ankit.web.crawler.data.Link;
import com.ankit.web.crawler.data.SharedData;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;

/**
 * Worker implementation for
 * fetching a link from internet
 * in html format
 */
@Slf4j
public class Fetcher extends Worker {

    /**
     * The Data.
     */
    SharedData data;
    /**
     * The Page rank policy.
     */
    PageRankPolicy pageRankPolicy;
    /**
     * The Crawling policy.
     */
    CrawlingPolicy crawlingPolicy;
    /**
     * The Thread name.
     */
    String threadName;
    /**
     * The Thread id.
     */
    Integer threadId;


    /**
     * Instantiates a new Fetcher.
     *
     * @param data           the data
     * @param pageRankPolicy the page rank policy
     * @param crawlingPolicy the crawling policy
     * @param threadId       the thread id
     */
    public Fetcher(SharedData data, PageRankPolicy pageRankPolicy, CrawlingPolicy crawlingPolicy, int threadId) {
        this.data = data;
        this.pageRankPolicy = pageRankPolicy;
        this.crawlingPolicy = crawlingPolicy;
        this.threadId = threadId;
        this.threadName = this.getClass().getName() + "_" + threadId;
    }

    /**
     * Worker implementation to fetch given link
     * and crawl it
     */
    public void run() {
        Thread.currentThread().setName(threadName);
        log.info("Starting Fetcher worker thread " + Thread.currentThread().getName());
        while (true) {
            try {
                while (data.getQueue().isEmpty()) {
                    data.getIdleURLFetcherThreadsPool().add(threadName);
                    log.info(String.format("No tasks in queue to execute, worker %s slppeing.... for %s s.",
                            threadName, Constants.THREAD_SLEEP_INTERVAL_IN_SECS));
                    Thread.sleep(Constants.THREAD_SLEEP_INTERVAL_IN_SECS * 10000);
                }


                data.getIdleURLFetcherThreadsPool().remove(threadName);
                Pair<Integer, Link> currentLink = data.getQueue().take();
                Link link = currentLink.getValue1();

                link.crawl();
                link.computePageRank(pageRankPolicy);

                Integer depth = currentLink.getValue0();
                data.getProcessedLink().add(new Pair<Integer, Link>(depth, link));

                if (depth > 0) {
                    for (Link childLink : crawlingPolicy.filterLinksToCrawl(link.getChildLinks())) {
                        synchronized (this) {
                            String uniqueLink = childLink.getFormattedUniqueLink();

                            // fast check for definately not processed with bloom filter to avoid latency,
                            // if possibly not processed then slow check for definately not processed with set.
                            if (!data.isURLPosiiblyProcessed(uniqueLink) || !data.isURLProcessed(uniqueLink)) {
                                data.addURLToProcessedSet(link.getFormattedUniqueLink());
                                data.getQueue().add(new Pair<>(depth - 1, childLink));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error("[Exception] Thread " + Thread.currentThread().getName() + " terminated with error " + e);
            }
        }
    }
}
