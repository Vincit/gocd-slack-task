package com.vincit.go.task.slack.utils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

public class FileReader {

    public static final String FILE_ENCODING = "UTF-8";

    public String getFileContents(String filePath) throws IOException {
        return IOUtils.toString(getClass().getResourceAsStream(filePath), FILE_ENCODING);
    }

}
