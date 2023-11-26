package com.ankit.web.crawler.executors;

import com.ankit.web.crawler.data.Link;

import java.util.List;

/**
 * Abstract class to represent crawling policy
 */
public abstract class CrawlingPolicy {
    /**
     * Filter links to crawl list.
     *
     * @param links the links
     * @return the list
     */
    public abstract List<Link> filterLinksToCrawl(List<Link> links);
}
