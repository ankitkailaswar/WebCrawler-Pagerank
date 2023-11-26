package com.ankit.web.crawler.executors;

import com.ankit.web.crawler.data.Constants;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Triplet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Output representing file
 */
@Slf4j
public class FileOutput extends Output {

    /**
     *
     */
    private String fileName;

    /**
     * Instantiates a new File output.
     *
     * @param fileName the file name
     * @throws IOException the io exception
     */
    public FileOutput(String fileName) throws IOException {
        this.fileName = fileName;
        if (Files.exists(Paths.get(fileName))) {
            String oldFileName = fileName + "." + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            Files.move(Paths.get(fileName), Paths.get(oldFileName));
        }

        Files.createFile(Paths.get(fileName));

        Triplet<String, String, String> header = new Triplet<>(Constants.OUTPUT_FILE_HEADER_COL1,
                Constants.OUTPUT_FILE_HEADER_COL2, Constants.OUTPUT_FILE_HEADER_COL3);
        List<Triplet<String, String, String>> headerData = new ArrayList<>();
        headerData.add(header);
        String headerString = super.formatOutput(headerData);
        Files.write(Paths.get(fileName), headerString.getBytes());
    }

    /**
     * Persists data to file
     * @param data the data
     * @return
     */
    public boolean persist(List<Triplet<String, String, String>> data) {
        try {
            Path path = Paths.get(fileName);
            Files.write(path, super.formatOutput(data).getBytes(), StandardOpenOption.APPEND);
            log.info(String.format("Added %s rows in file %s.", data.size(), fileName));
            return true;
        } catch (IOException e) {
            log.error(String.format("Failed to append given chunk of data %s to file.", data));
            return false;
        }
    }
}
