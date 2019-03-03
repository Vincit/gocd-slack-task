package com.vincit.go.task.slack.utils;

import com.thoughtworks.go.plugin.api.logging.Logger;
import in.ashwanthkumar.utils.lang.StringUtils;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

public class FileReader {

    public static final String FILE_ENCODING = "UTF-8";
    
    private Logger logger = Logger.getLoggerFor(FileReader.class);

    public String getFileContents(String filePath) throws IOException {
        return IOUtils.toString(getClass().getResourceAsStream(filePath), FILE_ENCODING);
    }
    
    public String getFileContents(String... filePaths) {
        for (String filePath : filePaths) {
            try {
                if (StringUtils.isNotEmpty(filePath)) {
                    logger.debug("Reading file '" + filePath + "'");
                    return getFileContents(filePath);
                } else {
                    logger.debug("Null/empty path, ignore");
                }
            } catch (IOException e) {
                logger.debug("Could not read file '" + filePath + "'");
            }
        }
        return null;
    }

}
