package com.lab;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GradeServiceTest {

    GradeService service = new GradeService();

    @Test void testXuatSac() { assertEquals("Xuất sắc", service.classify(9.0)); }
    @Test void testGioi() { assertEquals("Giỏi", service.classify(7.5)); }
    @Test void testKha() { assertEquals("Khá", service.classify(6.0)); }
    @Test void testTrungBinh() { assertEquals("Trung bình", service.classify(4.5)); }
    @Test void testYeu() { assertEquals("Yếu", service.classify(3.0)); }
    @Test void testBoundary85() { assertEquals("Xuất sắc", service.classify(8.5)); }
    @Test void testBoundary70() { assertEquals("Giỏi", service.classify(7.0)); }
    @Test void testBoundary55() { assertEquals("Khá", service.classify(5.5)); }
    @Test void testBoundary40() { assertEquals("Trung bình", service.classify(4.0)); }
    @Test void testInvalidNegative() {
        assertThrows(IllegalArgumentException.class, () -> service.classify(-1));
    }
    @Test void testInvalidOver10() {
        assertThrows(IllegalArgumentException.class, () -> service.classify(11));
    }
    @Test void testAverage() {
        assertEquals(7.5, service.average(new double[]{8.0, 7.0}), 0.001);
    }
    @Test void testAverageEmpty() {
        assertThrows(IllegalArgumentException.class, () -> service.average(new double[]{}));
    }
}
