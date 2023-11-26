package com.ankit.web.crawler.data;

import com.ankit.web.crawler.executors.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.ini4j.Ini;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;

/**
 * The type Configuration class
 * for all configuration at application level
 */
@Slf4j
public class Configuration {

    /**
     * number of worker threads
     * for crawler
     */
    @Getter
    Integer numWorkers;

    /**
     * Kind of crawling (html, css etc)
     */
    @Getter
    Constants.CRAWL_TYPES crawlType;

    /**
     * Size of tasks(link) que
     * to be crawled
     */
    @Getter
    Integer queueSize;

    /**
     * Approximately expected
     * links w.r.t depth of crawling.
     */
    @Getter
    Integer totalExpectedLinks;

    /**
     * The Bloom filter's probability
     * to miss
     */
    @Getter
    Double bloomFilterProbability;

    /**
     * The Page rank policy.
     * rules to calculate page rank
     */
    @Getter
    PageRankPolicy pageRankPolicy;

    /**
     * The Crawling policy.
     * e.g. crawl on http & https
     */
    @Getter
    CrawlingPolicy crawlingPolicy;

    /**
     * The Output type to serialised result.
     */
    @Getter
    Output output;

    /**
     * The Output file name.
     */
    @Getter
    String outputFileName;

    /**
     * The list of initial Urls to be crawled.
     */
    @Getter
    List<String> urls;

    /**
     * The Output type.
     */
    @Getter
    Constants.Output outputType;

    /**
     * Eastimated Max result size.
     */
    @Getter
    Integer maxResultSize;

    /**
     * Instantiates a new Configuration.
     *
     * @param fileName the file name
     * @param env      the env
     * @param urls     the urls
     */
    public Configuration(String fileName, String env, List<String> urls) {
        try {
            Ini ini = new Ini(new FileReader(fileName));
            Ini.Section section = ini.get(env);

            crawlType = Constants.CRAWL_TYPES.get(section.getOrDefault(Constants.CRAWL_TYPE_PARAM,
                    Constants.CRAWL_TYPE_PARAM_DEFAULT_VAL));

            numWorkers = Integer.parseInt(section.getOrDefault(Constants.NUM_WORKERS_PARAM,
                    Constants.NUM_WORKERS_DEFAULT_VAL.toString()));

            queueSize =  Integer.parseInt(section.getOrDefault(Constants.QUEUE_SIZE_PARAM,
                    Constants.QUEUE_SIZE_DEFAULT_VAL.toString()));

            totalExpectedLinks =  Integer.parseInt(section.getOrDefault(Constants.EXPECTED_LINKS_PARAM,
                    Constants.EXPECTED_LINKS_DEFAULT_VAL.toString()));

            bloomFilterProbability =  Double.parseDouble(section.getOrDefault(Constants.BLOOM_FILTER_MISS_PROBABILITY_PARAM,
                    Constants.BLOOM_FILTER_MISS_PROBABILITY_DEFAULT_VAL.toString()));

            if (urls.size() == 1) {
                String url = urls.get(0);
                try {
                    URL aURL = new URL(url);
                    outputFileName =  Paths.get("src", "main", "resources",
                            aURL.getHost() + (aURL.getPath() != null && !aURL.getPath().isEmpty() && !aURL.getPath().equals("/") ? aURL.getPath() : ""))
                            .toFile().getAbsolutePath();;
                } catch (Exception e) {
                    outputFileName = Paths.get("src", "main", "resources", section.getOrDefault(Constants.OUTPUT_FILE_PARAM,
                            Constants.OUTPUT_FILE_DEFAULT_VAL)).toFile().getAbsolutePath();
                }
            } else {
                outputFileName = Paths.get("src", "main", "resources", section.getOrDefault(Constants.OUTPUT_FILE_PARAM,
                        Constants.OUTPUT_FILE_DEFAULT_VAL)).toFile().getAbsolutePath();
            }

            /**
             * TODO : read it from conf.ini
             */
            outputType = Constants.Output.FILE;

            this.urls = urls;
            this.maxResultSize = Integer.parseInt(section.getOrDefault(Constants.RESULT_SIZE_PARAM,
                    Constants.RESULT_SIZE_DEFAULT_VAL.toString()));

            /**
             * TODO : convert to factory pattern like Outputter instead of hardcoding here.
             */
            pageRankPolicy = new InternalExternalPageRatioPolicy();
            crawlingPolicy  = new CrawlByProtocol(new HashSet<>(){{add(Constants.PROTOCOL.HTTP); add(Constants.PROTOCOL.HTTPS);}});

            log.info("Configuration initialized successfully.");
            return;
        } catch (IOException e) {
            log.warn("unable to find conf file, using default conf values.");
        }
    }
}
