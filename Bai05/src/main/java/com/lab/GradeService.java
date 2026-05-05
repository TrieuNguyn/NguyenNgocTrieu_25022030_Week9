package com.lab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Service for classifying student grades. */
public class GradeService {

    private static final Logger logger = LoggerFactory.getLogger(GradeService.class);

    /**
     * Classifies a GPA score into grade category.
     *
     * @param gpa score between 0.0 and 10.0
     * @return grade string
     */
    public String classify(double gpa) {
        if (gpa < 0 || gpa > 10) {
            logger.error("Invalid GPA value: {}", gpa);
            throw new IllegalArgumentException("GPA must be between 0 and 10");
        }
        String grade;
        if (gpa >= 8.5) {
            grade = "Xuất sắc";
        } else if (gpa >= 7.0) {
            grade = "Giỏi";
        } else if (gpa >= 5.5) {
            grade = "Khá";
        } else if (gpa >= 4.0) {
            grade = "Trung bình";
        } else {
            grade = "Yếu";
        }
        logger.info("GPA {} classified as: {}", gpa, grade);
        return grade;
    }

    /**
     * Calculates average GPA from array of scores.
     *
     * @param scores array of GPA scores
     * @return average value
     */
    public double average(double[] scores) {
        if (scores == null || scores.length == 0) {
            throw new IllegalArgumentException("Scores array must not be empty");
        }
        double sum = 0;
        for (double s : scores) {
            sum += s;
        }
        return sum / scores.length;
    }
}
