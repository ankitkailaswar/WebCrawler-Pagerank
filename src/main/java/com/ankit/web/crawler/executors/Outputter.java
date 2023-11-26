package com.ankit.web.crawler.executors;

import com.ankit.web.crawler.data.Configuration;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Factory class to represent output
 * type to be instantiated.
 */
@Slf4j
public class Outputter {
    /**
     * Gets output type.
     *
     * @param conf the conf
     * @return the output type
     * @throws IOException the io exception
     */
    public static Output getOutputType(Configuration conf) throws IOException {
        switch (conf.getOutputType()) {
            case CONSOLE:
                System.out.println("File output is not yet supported.");
                log.debug("File output is not yet supported");
                return null;
            case FILE:
                return new FileOutput(conf.getOutputFileName());
            case STREAM:
                System.out.println("Stream output is not yet supported.");
                log.debug("Stream output is not yet supported");
                return null;
            default:
                log.warn("Using default outputter as console since invalid output type is mentioned.");
                return new FileOutput(conf.getOutputFileName());
        }
    }
}
