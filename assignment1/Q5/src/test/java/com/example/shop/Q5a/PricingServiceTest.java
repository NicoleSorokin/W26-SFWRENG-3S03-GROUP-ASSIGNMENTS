package com.example.shop.Q5a;

import com.example.shop.Order;
import com.example.shop.OrderItem;
import com.example.shop.PricingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PricingServiceTest {

    private PricingService pricingService;

    @BeforeEach
    void setUp() {
        pricingService = new PricingService();
    }

    @Test
    void testCalculateSubtotalForSingleItem() {
        Order order = new Order();
        order.addItem(new OrderItem("Laptop", 1, 999.99));
        double subtotal = pricingService.calculateSubtotal(order);
        assertEquals(999.99, subtotal, 0.001);
    }

    @Test
    void testCalculateTaxOnPositiveSubtotal() {
        double tax = pricingService.calculateTax(100.0);
        assertEquals(20.0, tax, 0.001);
    }
}
