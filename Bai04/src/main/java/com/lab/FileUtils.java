package com.lab;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for cross-platform file path operations.
 * Uses java.nio.file.Path to handle OS-specific path separators.
 */
public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * Builds a file path from components using the OS-native separator.
     * Uses Path API instead of hardcoded "/" or "\" for cross-platform compatibility.
     *
     * @param parts path components (directory names, filename)
     * @return resolved path string
     */
    public String buildPath(String... parts) {
        if (parts == null || parts.length == 0) {
            throw new IllegalArgumentException("Path parts must not be empty");
        }
        Path path = Paths.get(parts[0], java.util.Arrays.copyOfRange(parts, 1, parts.length));
        String result = path.toString();
        logger.info("Built path: {}", result);
        return result;
    }

    /**
     * Returns the file name (last component) of a path string.
     *
     * @param pathStr full path string
     * @return filename portion
     */
    public String getFileName(String pathStr) {
        Path path = Paths.get(pathStr);
        Path fileName = path.getFileName();
        if (fileName == null) {
            return "";
        }
        return fileName.toString();
    }
}
