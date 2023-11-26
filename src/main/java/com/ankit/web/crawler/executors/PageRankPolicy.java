package com.ankit.web.crawler.executors;

import com.ankit.web.crawler.data.Link;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Abstract class to represent page rang calculation policy.
 */
@Slf4j
public abstract class PageRankPolicy {
    /**
     * Compute page rank double.
     *
     * @param links      the links
     * @param parentLink the parent link
     * @return the double
     */
    public abstract Double computePageRank(List<Link> links, Link parentLink);
}
