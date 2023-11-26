package com.ankit.web.crawler.executors;

import lombok.extern.slf4j.Slf4j;

/**
 * Abstract class to represent worker
 * running under service
 */
@Slf4j
public abstract class Worker extends Thread {
    public abstract void run();
}
