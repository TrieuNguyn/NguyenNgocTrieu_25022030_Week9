package com.lab;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Cross-platform file path tests.
 * Uses File.separator and Path API — NO hardcoded "/" or "\" separators.
 */
class FileUtilsTest {

    FileUtils utils = new FileUtils();

    /**
     * DEMO LỖI (đã sửa):
     * Code cũ bị lỗi: assertEquals("data/reports/file.txt", utils.buildPath("data","reports","file.txt"))
     * Trên Windows dấu \ được dùng thay vì / → test fail.
     * Sửa: dùng Paths.get() để build expected path thay vì hardcode.
     */
    @Test
    void testBuildPath_crossPlatform() {
        String expected = Paths.get("data", "reports", "file.txt").toString();
        assertEquals(expected, utils.buildPath("data", "reports", "file.txt"));
    }

    @Test
    void testBuildPath_singleComponent() {
        String expected = Paths.get("readme.txt").toString();
        assertEquals(expected, utils.buildPath("readme.txt"));
    }

    @Test
    void testGetFileName() {
        // Build path cross-platform then extract filename
        String fullPath = Paths.get("home", "user", "document.pdf").toString();
        assertEquals("document.pdf", utils.getFileName(fullPath));
    }

    @Test
    void testBuildPath_emptyThrows() {
        assertThrows(IllegalArgumentException.class, () -> utils.buildPath());
    }

    @Test
    void testFileSeparatorIsConsistent() {
        // Xác nhận separator từ JVM nhất quán với Path API
        String builtPath = utils.buildPath("a", "b");
        assertTrue(builtPath.contains(File.separator) || builtPath.equals("a" + File.separator + "b"));
    }
}
