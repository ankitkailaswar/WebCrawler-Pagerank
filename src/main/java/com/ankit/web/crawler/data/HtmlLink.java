package com.ankit.web.crawler.data;

import com.ankit.web.crawler.executors.PageRankPolicy;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Implementation Link for html page crawling
 */
@Slf4j
public class HtmlLink extends Link {

    /**
     * Instantiates a new Html link.
     *
     * @param link the link
     */
    public HtmlLink(String link) {
        super(link);
    }

    /**
     * compute page rank for current link
     * @param policy the policy to compute page rank
     */
    public void computePageRank(PageRankPolicy policy) {
        super.rank = policy.computePageRank(super.childLinks, this);
    }

    /**
     * Crawls current link
     */
    public void crawl() {
        try {

            Document doc = Jsoup.connect(getUrlLink())
                    .userAgent(Constants.DEFAULT_USER_AGENT)
                    .referrer(Constants.DEFAULT_REFERER)
                    .get();

            if (doc.outputSettings().syntax().name().equals(Constants.CRAWL_TYPES.HTML.getName())) {
                Elements availableLinksOnPage = doc.select(Constants.HTML_PAGE_LINK_REF);
                for (Element page : availableLinksOnPage) {
                    String linkString = page.attr(Constants.HTML_PAGE_REF);
                    if (validateChildLink(linkString))
                        childLinks.add(new HtmlLink(linkString));
                }
            }
        } catch (IOException e) {
            log.error(String.format("Failed to get child url link from html doc for %s. Error : %s",
                    super.getUrlLink(), e.toString()));
        }
    }
}
