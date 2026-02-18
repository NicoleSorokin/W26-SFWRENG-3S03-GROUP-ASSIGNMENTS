package com.example.shop.Q5b;

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
    void testCalculateSubtotalForEmptyOrder() {
        Order order = new Order();
        double subtotal = pricingService.calculateSubtotal(order);
        assertEquals(0.0, subtotal, 0.001);
    }

    @Test
    void testCalculateSubtotalForSingleItem() {
        Order order = new Order();
        order.addItem(new OrderItem("Laptop", 1, 999.99));
        double subtotal = pricingService.calculateSubtotal(order);
        assertEquals(999.99, subtotal, 0.001);
    }

    @Test
    void testCalculateSubtotalForMultipleItems() {
        Order order = new Order();
        order.addItem(new OrderItem("Laptop", 1, 999.99));
        order.addItem(new OrderItem("Mouse", 2, 25.50));
        order.addItem(new OrderItem("Keyboard", 1, 79.99));
        double subtotal = pricingService.calculateSubtotal(order);
        assertEquals(1130.98, subtotal, 0.001);
    }

    @Test
    void testCalculateSubtotalWithMultipleQuantities() {
        Order order = new Order();
        order.addItem(new OrderItem("USB Cable", 5, 10.00));
        double subtotal = pricingService.calculateSubtotal(order);
        assertEquals(50.0, subtotal, 0.001);
    }

    @Test
    void testCalculateTaxOnZeroSubtotal() {
        double tax = pricingService.calculateTax(0.0);
        assertEquals(0.0, tax, 0.001);
    }

    @Test
    void testCalculateTaxOnPositiveSubtotal() {
        double tax = pricingService.calculateTax(100.0);
        assertEquals(20.0, tax, 0.001);
    }

    @Test
    void testCalculateTaxOnLargeSubtotal() {
        double tax = pricingService.calculateTax(5000.0);
        assertEquals(1000.0, tax, 0.001);
    }

    @Test
    void testCalculateTaxOnSmallSubtotal() {
        double tax = pricingService.calculateTax(10.0);
        assertEquals(2.0, tax, 0.001);
    }

    @Test
    void testCalculateTaxWithDecimals() {
        double tax = pricingService.calculateTax(99.99);
        assertEquals(19.998, tax, 0.001);
    }

    @Test
    void testCalculateTaxOnNegativeSubtotalThrowsException() {
        assertThrows(IllegalArgumentException.class, 
            () -> pricingService.calculateTax(-10.0));
    }

    @Test
    void testCalculateTaxOnVerySmallSubtotal() {
        double tax = pricingService.calculateTax(0.01);
        assertEquals(0.002, tax, 0.0001);
    }

    @Test
    void testTaxRate() {
        // Verify the 20% tax rate
        double tax = pricingService.calculateTax(500.0);
        assertEquals(100.0, tax, 0.001);
        
        tax = pricingService.calculateTax(250.0);
        assertEquals(50.0, tax, 0.001);
    }

    @Test
    void testSubtotalWithFreeItems() {
        Order order = new Order();
        order.addItem(new OrderItem("Free Sample", 3, 0.0));
        double subtotal = pricingService.calculateSubtotal(order);
        assertEquals(0.0, subtotal, 0.001);
    }

    @Test
    void testSubtotalWithMixedPrices() {
        Order order = new Order();
        order.addItem(new OrderItem("Expensive", 1, 1000.00));
        order.addItem(new OrderItem("Cheap", 10, 1.00));
        order.addItem(new OrderItem("Free", 5, 0.0));
        double subtotal = pricingService.calculateSubtotal(order);
        assertEquals(1010.00, subtotal, 0.001);
    }
}
