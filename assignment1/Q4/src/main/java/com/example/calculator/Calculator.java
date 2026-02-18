package com.example.calculator;

/*
Old code from previous iteration:

public class Calculator {

    public double divide(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        return 2.0;
    }
}
*/

public class Calculator {

    public double divide(double numerator, double denominator) {
        if (denominator == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        return numerator / denominator;
    }
}
