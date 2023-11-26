package com.ankit.web.crawler.data;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Represent data objects shared across all services and
 * worker for common CRUD
 */
@Slf4j
public class SharedData implements Serializable {

    @Getter
    private Set<String> visitedURLs;

    private BloomFilter<String> filter;

    @Getter
    private List<Pair<Integer, Link>> processedLink;

    @Getter
    private Set<String> idleURLFetcherThreadsPool;

    @Getter
    private BlockingQueue<Pair<Integer,Link>> queue;

    @Getter
    private Integer initialDepth;

    /**
     * Instantiates a new Shared data.
     *
     * @param expectedLinks         the expected links
     * @param bloomFilterProbaility the bloom filter probaility
     * @param queueCapacity         the queue capacity
     * @param urls                  the urls
     * @param initialDepth          the initial depth
     */
    public SharedData(Integer expectedLinks, Double bloomFilterProbaility, int queueCapacity, List<String> urls, int initialDepth) {
        visitedURLs = Collections.synchronizedSet(new HashSet<>());
        idleURLFetcherThreadsPool = Collections.synchronizedSet(new HashSet<>());
        processedLink = Collections.synchronizedList(new LinkedList<>());
        filter = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_16), expectedLinks, bloomFilterProbaility);
        queue = new ArrayBlockingQueue<>(queueCapacity);
        this.initialDepth = initialDepth;

        for (String url : urls) {
            /**
             * TODO : convert this to factory pattern based on CRAWL_TYPE_PARAM_DEFAULT_VAL
             */
            Link currentLink = new HtmlLink(url);
            currentLink.crawl();
            queue.add(new Pair<>(initialDepth, currentLink));
        }
    }

    /**
     * Predits if url is processed or not with probability.
     *
     * @param url the url
     * @return the boolean
     */
    public synchronized boolean isURLPosiiblyProcessed(String url) {
        return filter.mightContain(url);
    }

    /**
     * Definately finds if url is already processed or not.
     *
     * @param url the url
     * @return the boolean
     */
    public synchronized boolean isURLProcessed(String url) {
        return visitedURLs.contains(url);
    }

    /**
     * Add url to processed set boolean.
     *
     * @param url the url
     * @return the boolean
     */
    public synchronized boolean addURLToProcessedSet(String url) {
        return filter.put(url) && visitedURLs.add(url);
    }


}
