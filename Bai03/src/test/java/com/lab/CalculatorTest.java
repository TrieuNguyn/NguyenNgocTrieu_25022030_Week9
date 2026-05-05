package com.lab;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
dcm
    Calculator calc = new Calculator();
    @Test void testAdd() { assertEquals(5, calc.add(2, 3)); }
    @Test void testMultiply() { assertEquals(20, calc.multiply(4, 5)); }
    @Test void testDivide() { assertEquals(2.5, calc.divide(5, 2), 0.001); }
    @Test void testDivideByZero() {
        assertThrows(IllegalArgumentException.class, () -> calc.divide(10, 0));
    }
}
