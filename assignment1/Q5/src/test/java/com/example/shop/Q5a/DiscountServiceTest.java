package com.example.shop.Q5a;

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
    void testStudent10Discount() {
        double result = discountService.applyDiscount(100.0, "STUDENT10");
        assertEquals(90.0, result, 0.001);
    }

    @Test
    void testInvalidCodeReturnsOriginalPrice() {
        double result = discountService.applyDiscount(100.0, "INVALID");
        assertEquals(100.0, result, 0.001);
    }
}
