package com.ankit.web.crawler.data;

import com.ankit.web.crawler.executors.PageRankPolicy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class to represnt a http link
 */
@Slf4j
public abstract class Link {

    /**
     * The Url link.
     */
    @Getter
    protected String urlLink;

    /**
     * The Child links present in html content of given link
     */
    @Getter
    protected List<Link> childLinks;

    /**
     * The Rank of a link
     */
    @Getter
    protected Double rank;

    @Getter
    private String protocol;

    @Getter
    private String authority;

    @Getter
    private String host;

    @Getter
    private Integer port;

    @Getter
    private String path;

    @Getter
    private String query;

    @Getter
    private String fileName;

    @Getter
    private String ref;

    /**
     * Instantiates a new Link.
     *
     * @param urlLink the url link
     */
    public Link(String urlLink) {
        this.urlLink = urlLink;
        childLinks = new ArrayList<>();
        parseLink();
    }

    /**
     * Crawl.
     */
    public abstract void crawl();

    /**
     * Compute page rank.
     *
     * @param policy the policy
     */
    public abstract void computePageRank(PageRankPolicy policy);

    /**
     * Parse link and init its different attributes
     */
    protected void parseLink() {
        try {
            URL aURL = new URL(urlLink);
            host = aURL.getHost();
            protocol = aURL.getProtocol();
            authority = aURL.getAuthority();
            port = aURL.getPort();
            path = aURL.getPath();
            query = aURL.getQuery();
            fileName = aURL.getFile();
            ref = aURL.getRef();
        } catch (Exception e) {
            log.error(String.format("Failed to identify protocol for link %s, Error : %s", urlLink.toString(), e.toString()));
        }
        return;
    }

    /**
     * Gets formatted unique link.
     *
     * @return the formatted unique link
     */
    public String getFormattedUniqueLink() {
        return host + ":" + (port == null || port == -1 ? "80" : port.toString()) + (path == null || path.isEmpty() ? "/" : path);
    }

    /**
     * Validate child link boolean.
     *
     * @param link the link
     * @return the boolean
     */
    protected boolean validateChildLink(String link) {
        if (link == null || link.isEmpty() || !link.matches(".*\\w.*")) {
            return false;
        }
        return true;
    }

    /**
     * Gets link.
     *
     * @return the link
     */
    public Pair<String, String> getLink() {
        return new Pair<>(urlLink, rank.toString());
    }
}
