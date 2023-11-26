package com.ankit.web.crawler.executors;

import com.ankit.web.crawler.data.Constants;
import com.ankit.web.crawler.data.Link;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation to crawl by kind of protocol
 * http, ftp etc
 */
@Slf4j
public class CrawlByProtocol extends CrawlingPolicy {

    /**
     * The Allowed protocols.
     */
    Set<Constants.PROTOCOL> allowedProtocols;

    /**
     * Instantiates a new Crawl by protocol.
     *
     * @param allowedProtocols the allowed protocols
     */
    public CrawlByProtocol(Set<Constants.PROTOCOL> allowedProtocols) {
        this.allowedProtocols = new HashSet<>(allowedProtocols);
    }

    /**
     *
     * @param links the links
     * @return
     */
    public List<Link> filterLinksToCrawl(List<Link> links) {
        List<Link> filteredLinks = new ArrayList<>();
        for (Link link : links) {
            if (allowedProtocols.contains(Constants.PROTOCOL.get(link.getProtocol())))
                filteredLinks.add(link);
        }
        return filteredLinks;
    }
}
