package com.ankit.web.crawler.executors;

import com.ankit.web.crawler.data.Link;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Policy to compute rank based on
 * external and internal reference in
 * page content based on domain name.
 */
@Slf4j
public class InternalExternalPageRatioPolicy extends PageRankPolicy {
    /**
     * computes rank of page
     * @param links      the links
     * @param parentLink the parent link
     * @return
     */
    public Double computePageRank(List<Link> links, Link parentLink) {
        if (links == null || links.size() == 0) return 0.0;

        Integer totalLinks = 0;
        Integer internalLinks = 0;
        for (Link link : links) {
            totalLinks += 1;
            internalLinks += link.getHost().equals(parentLink.getHost()) ? 1 : 0;
        }
        return Double.parseDouble(internalLinks.toString())/Double.parseDouble(totalLinks.toString());
    }

}
