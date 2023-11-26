package com.ankit.web.crawler.executors;

import com.ankit.web.crawler.data.Configuration;
import com.ankit.web.crawler.data.Link;
import com.ankit.web.crawler.data.SharedData;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.List;

/**
 * Servoce implementation to persist
 * processed links for ranking and crawling
 */
@Slf4j
public class PersistService extends Service {

    private Output output;

    /**
     * Instantiates a new Persist service.
     *
     * @param data   the data
     * @param conf   the conf
     * @param output the output
     */
    public PersistService(SharedData data, Configuration conf, Output output) {
        super(data, conf);
        this.output = output;
    }

    /**
     * service implementation to persist result set
     * to selected output type
     */
    public void run() {
        Thread.currentThread().setName("PersistService");

        while (true) {
            if (data.getProcessedLink().size() > conf.getMaxResultSize()) {

                log.debug("Found result set, persisting....");

                List<Pair<Integer, Link>> chunk = null;
                synchronized (data.getProcessedLink()) {
                    chunk = new ArrayList<>(data.getProcessedLink().subList(0, conf.getMaxResultSize() - 1));
                    data.getProcessedLink().subList(0, conf.getMaxResultSize() - 1).clear();
                }

                List<Triplet<String, String, String>> toBeSerialisedData = new ArrayList<>();
                for (Pair<Integer, Link> link : chunk) {
                    Pair<String, String> linkData = link.getValue1().getLink();
                    Integer level = data.getInitialDepth() - link.getValue0() + 1;

                    toBeSerialisedData.add(new Triplet<>(level.toString(), linkData.getValue0(), linkData.getValue1()));
                }
                output.persist(toBeSerialisedData);

                log.debug("persisted data.");

            }
            try {
                log.info("PersistService sleeping for 3 mins.");
                Thread.sleep(3 * 60 * 1000);
            } catch (InterruptedException e) {
                log.error(String.format("Sleep interupted for persist service. Error : ", e.toString()));
            }
        }
    }
}