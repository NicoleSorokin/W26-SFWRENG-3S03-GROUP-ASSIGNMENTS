package com.example.calculator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CalculatorDivideTest {

    private final Calculator calculator = new Calculator();

    @Test
    public void divideTwoPositiveNumbers() {
        double result = calculator.divide(12, 6);
        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void divideByZeroShouldThrowException() {
        assertThrows(ArithmeticException.class, () -> calculator.divide(1, 0));
    }

    /**
     * Added tests for improved test suite in part d)
     */

    @Test
    public void divideDifferentPositiveNumbers() {
        double result = calculator.divide(9, 3);
        assertEquals(3.0, result, 0.0001);
    }

    @Test
    public void divideSmallerNumberByLargerNumber() {
        double result = calculator.divide(1, 2);
        assertEquals(0.5, result, 0.0001);
    }

    @Test
    public void dividePositiveByNegative() {
        double result = calculator.divide(-12, 2);
        assertEquals(-6.0, result, 0.0001);
    }

    @Test
    public void divideTwoNegativeNumbers() {
        double result = calculator.divide(-10, -2);
        assertEquals(5.0, result, 0.0001);
    }

    @Test
    public void divideByOne() {
        double result = calculator.divide(5, 1);
        assertEquals(5.0, result, 0.0001);
    }
}