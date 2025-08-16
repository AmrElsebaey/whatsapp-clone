package com.elsebaey.whatsappclone.file;

import ch.qos.logback.core.util.StringUtil;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class FileUtils {

    private FileUtils() {}

    public static byte[] readFileFromLocation(String fileUrl) {
        if(StringUtils.isBlank(fileUrl)) {
            log.warn("File URL is blank or null");
            return new byte[0];
        }
        try {
            Path path = new File(fileUrl).toPath();
            return Files.readAllBytes(path);
        } catch (IOException e) {
            log.warn("Failed to read file from location: {}", fileUrl, e);
        }
        return new byte[0];
    }
}
