package com.example.shop.Q5b;

import com.example.shop.DiscountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiscountServiceTest {

    private DiscountService discountService;

    @BeforeEach
    void setUp() {
        discountService = new DiscountService();
    }

    @Test
    void testNoDiscountWithNullCode() {
        double result = discountService.applyDiscount(100.0, null);
        assertEquals(100.0, result, 0.001);
    }

    @Test
    void testNoDiscountWithEmptyCode() {
        double result = discountService.applyDiscount(100.0, "");
        assertEquals(100.0, result, 0.001);
    }

    @Test
    void testNoDiscountWithBlankCode() {
        double result = discountService.applyDiscount(100.0, "   ");
        assertEquals(100.0, result, 0.001);
    }

    @Test
    void testStudent10DiscountLowercase() {
        double result = discountService.applyDiscount(100.0, "student10");
        assertEquals(90.0, result, 0.001);
    }

    @Test
    void testStudent10DiscountUppercase() {
        double result = discountService.applyDiscount(100.0, "STUDENT10");
        assertEquals(90.0, result, 0.001);
    }

    @Test
    void testStudent10DiscountMixedCase() {
        double result = discountService.applyDiscount(100.0, "StUdEnT10");
        assertEquals(90.0, result, 0.001);
    }

    @Test
    void testBlackFridayDiscountLowercase() {
        double result = discountService.applyDiscount(100.0, "blackfriday");
        assertEquals(70.0, result, 0.001);
    }

    @Test
    void testBlackFridayDiscountUppercase() {
        double result = discountService.applyDiscount(100.0, "BLACKFRIDAY");
        assertEquals(70.0, result, 0.001);
    }

    @Test
    void testBlackFridayDiscountMixedCase() {
        double result = discountService.applyDiscount(100.0, "BlackFriday");
        assertEquals(70.0, result, 0.001);
    }

    @Test
    void testInvalidCodeReturnsOriginalPrice() {
        double result = discountService.applyDiscount(100.0, "INVALID");
        assertEquals(100.0, result, 0.001);
    }

    @Test
    void testInvalidCodeLowercaseReturnsOriginalPrice() {
        double result = discountService.applyDiscount(100.0, "invalid");
        assertEquals(100.0, result, 0.001);
    }

    @Test
    void testUnrecognizedCodeReturnsOriginalPrice() {
        double result = discountService.applyDiscount(100.0, "UNKNOWN");
        assertEquals(100.0, result, 0.001);
    }

    @Test
    void testRandomCodeReturnsOriginalPrice() {
        double result = discountService.applyDiscount(100.0, "RANDOMCODE123");
        assertEquals(100.0, result, 0.001);
    }

    @Test
    void testDiscountOnZeroSubtotal() {
        double result = discountService.applyDiscount(0.0, "STUDENT10");
        assertEquals(0.0, result, 0.001);
    }

    @Test
    void testDiscountOnLargeSubtotal() {
        double result = discountService.applyDiscount(10000.0, "BLACKFRIDAY");
        assertEquals(7000.0, result, 0.001);
    }

    @Test
    void testDiscountOnSmallSubtotal() {
        double result = discountService.applyDiscount(5.0, "STUDENT10");
        assertEquals(4.5, result, 0.001);
    }

    @Test
    void testStudent10DiscountPrecision() {
        double result = discountService.applyDiscount(99.99, "STUDENT10");
        assertEquals(89.991, result, 0.001);
    }

    @Test
    void testBlackFridayDiscountPrecision() {
        double result = discountService.applyDiscount(123.45, "BLACKFRIDAY");
        assertEquals(86.415, result, 0.001);
    }
}
