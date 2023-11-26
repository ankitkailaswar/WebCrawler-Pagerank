package com.ankit.web.crawler.executors;

import com.ankit.web.crawler.data.Configuration;
import com.ankit.web.crawler.data.SharedData;
import lombok.extern.slf4j.Slf4j;

/**
 * Abstract class representing services running under
 * given application.
 */
@Slf4j
public abstract class Service extends Thread {
    /**
     * The Data.
     */
    SharedData data;
    /**
     * The Conf.
     */
    Configuration conf;

    /**
     * Instantiates a new Service.
     *
     * @param data the data
     * @param conf the conf
     */
    public Service(SharedData data, Configuration conf) {
        this.data = data;
        this.conf = conf;
    }

    public abstract void run();
}
