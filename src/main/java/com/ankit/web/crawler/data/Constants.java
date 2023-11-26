package com.ankit.web.crawler.data;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Set of Constants used in application.
 */
public class Constants {

    /**
     * param name for queue size
     */
    public static final  String QUEUE_SIZE_PARAM = "queue_size";
    /**
     * default value for task que size
     */
    public static final  Integer QUEUE_SIZE_DEFAULT_VAL = 100000;

    /**
     * param name for crawl type
     */
    public static final  String CRAWL_TYPE_PARAM = "crawl_type";
    /**
     * default crawl type
     */
    public static final  String CRAWL_TYPE_PARAM_DEFAULT_VAL = CRAWL_TYPES.HTML.getName();

    /**
     * param name for workers pool size.
     */
    public static final  String NUM_WORKERS_PARAM = "workers";
    /**
     * default worker pool size.
     */
    public static final  Integer NUM_WORKERS_DEFAULT_VAL = 5;

    /**
     * param name for expected links to be processed.
     */
    public static final  String EXPECTED_LINKS_PARAM = "expected_links";
    /**
     * default value for expected links to be processed
     */
    public static final  Integer EXPECTED_LINKS_DEFAULT_VAL = 10000000; /*10M*/

    /**
     * param name for bloom filter miss probability.
     */
    public static final  String BLOOM_FILTER_MISS_PROBABILITY_PARAM = "bloom_filter_miss_probaility";
    /**
     * .default value for bloom filter probability.
     */
    public static final  Double BLOOM_FILTER_MISS_PROBABILITY_DEFAULT_VAL = 0.001;

    /**
     * param name for o/p file.
     */
    public static final  String OUTPUT_FILE_PARAM = "file_name";
    /**
     * default value for output file.
     */
    public static final  String OUTPUT_FILE_DEFAULT_VAL = "data.txt";

    /**
     * param name for result size.
     */
    public static final  String RESULT_SIZE_PARAM = "max_result_size";
    /**
     * default value for result size.
     */
    public static final  Integer RESULT_SIZE_DEFAULT_VAL = 1000;

    /**
     * html tag for page ref.
     */
    public static final String HTML_PAGE_LINK_REF = "a[href]";

    /**
     * represents absolute reference to page
     */
    public static final String HTML_PAGE_REF = "abs:href";


    /**
     * default referer for crawling api
     */
    public static final String DEFAULT_REFERER = "http://www.google.com";

    /**
     * default user agent for crawling api
     */
    public static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36";


    /**
     * enum for supported Protocol.
     */
    public static enum PROTOCOL {
        /**
         * Http protocol.
         */
        HTTP("http"),
        /**
         * Https protocol.
         */
        HTTPS("https"),
        /**
         * Ftp protocol.
         */
        FTP("ftp");

        private String name;

        private static final Map<String, PROTOCOL> ENUM_MAP;

        PROTOCOL(String name) {
            this.name = name;
        }

        /**
         * Gets name.
         *
         * @return the name
         */
        public String getName() {
            return this.name;
        }

        static {
            Map<String, PROTOCOL> map = new ConcurrentHashMap<>();
            for (PROTOCOL instance : PROTOCOL.values()) {
                map.put(instance.getName().toLowerCase(),instance);
            }
            ENUM_MAP = Collections.unmodifiableMap(map);
        }

        /**
         * Get protocol.
         *
         * @param name the name
         * @return the protocol
         */
        public static PROTOCOL get (String name) {
            return ENUM_MAP.get(name.toLowerCase());
        }
    }

    /**
     * The enum for supported Crawl types.
     */
    public static enum CRAWL_TYPES {
        /**
         * Html crawl types.
         */
        HTML("html"),
        /**
         * Css crawl types.
         */
        CSS("css");

        private String name;

        private static final Map<String, CRAWL_TYPES> ENUM_MAP;

        CRAWL_TYPES (String name) {
            this.name = name;
        }

        /**
         * Gets name.
         *
         * @return the name
         */
        public String getName() {
            return this.name;
        }

        static {
            Map<String,CRAWL_TYPES> map = new ConcurrentHashMap<>();
            for (CRAWL_TYPES instance : CRAWL_TYPES.values()) {
                map.put(instance.getName().toLowerCase(),instance);
            }
            ENUM_MAP = Collections.unmodifiableMap(map);
        }

        /**
         * Get crawl types.
         *
         * @param name the name
         * @return the crawl types
         */
        public static CRAWL_TYPES get (String name) {
            return ENUM_MAP.get(name.toLowerCase());
        }
    }

    /**
     * The constant SPACE.
     */
    public static final String SPACE = " ";
    /**
     * The constant COL_DELIMITER.
     */
    public static final String COL_DELIMITER = "\t";
    /**
     * The constant ROW_DELIMITER.
     */
    public static final String ROW_DELIMITER = "\n";

    /**
     * Output file header name for url
     */
    public static final String OUTPUT_FILE_HEADER_COL1 = "url";

    /**
     * Output file header name for depth
     */
    public static final String OUTPUT_FILE_HEADER_COL2 = "depth";

    /**
     * Output file header name for rank
     */
    public static final String OUTPUT_FILE_HEADER_COL3 = "ratio";

    /**
     * The enum for supported Output.
     */
    public static enum Output {
        /**
         * represnet console output
         */
        CONSOLE, /* default */
        /**
         * represent file output
         */
        FILE,
        /**
         * represent streaming output
         */
        STREAM
    }

    /**
     * The constant THREAD_SLEEP_INTERVAL_IN_SECS.
     */
    public static final Integer THREAD_SLEEP_INTERVAL_IN_SECS = 10;

    /**
     * The constant APP_IDLE_TIMEOUT_IN_SECS.
     */
    /* 10 mins*/
    public static final Integer APP_IDLE_TIMEOUT_IN_SECS = 10 * 60;
    /**
     * The constant APP_POLL_WAIT_TIME.
     */
    /* 1 min*/
    public static final Integer APP_POLL_WAIT_TIME = 60;


}
