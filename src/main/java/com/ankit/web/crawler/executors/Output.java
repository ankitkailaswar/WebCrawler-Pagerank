package com.ankit.web.crawler.executors;

import com.ankit.web.crawler.data.Constants;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstarct claa to represent kind of output
 * to serialise result.
 */
@Slf4j
public abstract class Output {
    /**
     * Persist boolean.
     *
     * @param data the data
     * @return the boolean
     */
    public abstract boolean persist(List<Triplet<String, String, String>> data);

    /**
     * Format output string.
     *
     * @param data the data
     * @return the string
     */
    public String formatOutput(List<Triplet<String, String, String>> data) {

        List<Object> formattedData = new ArrayList<>();

        for (Triplet<String, String, String> row : data) {
            String serialisedRow = row.getValue1() + Constants.COL_DELIMITER +
                    row.getValue0() + Constants.COL_DELIMITER + row.getValue2();
            formattedData.add(serialisedRow);
        }
        return formattedData.stream().map(object->object.toString()).collect(Collectors.joining(Constants.ROW_DELIMITER));
    }
}
