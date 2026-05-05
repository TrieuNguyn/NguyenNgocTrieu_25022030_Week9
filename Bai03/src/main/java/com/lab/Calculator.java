package com.lab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Simple calculator with basic arithmetic operations. */
public class Calculator {

    private static final Logger logger = LoggerFactory.getLogger(Calculator.class);

    /** Adds two integers. */
    public int add(int a, int b) {
        int result = a + b;
        logger.info("add({}, {}) = {}", a, b, result);
        return result;
    }

    /** Multiplies two integers. */
    public int multiply(int a, int b) {
        int result = a * b;
        logger.info("multiply({}, {}) = {}", a, b, result);
        return result;
    }

    /** Divides dividend by divisor. */
    public double divide(double dividend, double divisor) {
        if (divisor == 0) {
            logger.error("Division by zero: dividend={}", dividend);
            throw new IllegalArgumentException("Divisor must not be zero");
        }
        double result = dividend / divisor;
        logger.info("divide({}, {}) = {}", dividend, divisor, result);
        return result;
    }

    /** Entry point for demonstration. */
    public static void main(String[] args) {
        Calculator calc = new Calculator();
        logger.info("Calculator started");
        System.out.println("2 + 3 = " + calc.add(2, 3));
        System.out.println("4 * 5 = " + calc.multiply(4, 5));
        System.out.println("10 / 4 = " + calc.divide(10, 4));
    }
}
