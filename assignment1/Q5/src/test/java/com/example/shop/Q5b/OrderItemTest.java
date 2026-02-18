package com.example.shop.Q5b;

import com.example.shop.OrderItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @Test
    void testValidOrderItemCreation() {
        OrderItem item = new OrderItem("Laptop", 2, 999.99);
        assertNotNull(item);
        assertEquals(2, item.getQuantity());
    }

    @Test
    void testTotalPriceCalculation() {
        OrderItem item = new OrderItem("Mouse", 3, 25.50);
        assertEquals(76.50, item.getTotalPrice(), 0.001);
    }

    @Test
    void testSingleQuantityItem() {
        OrderItem item = new OrderItem("Keyboard", 1, 79.99);
        assertEquals(79.99, item.getTotalPrice(), 0.001);
    }

    @Test
    void testLargeQuantity() {
        OrderItem item = new OrderItem("USB Cable", 100, 5.00);
        assertEquals(500.00, item.getTotalPrice(), 0.001);
    }

    @Test
    void testZeroQuantityThrowsException() {
        assertThrows(IllegalArgumentException.class, 
            () -> new OrderItem("Laptop", 0, 999.99));
    }

    @Test
    void testNegativeQuantityThrowsException() {
        assertThrows(IllegalArgumentException.class, 
            () -> new OrderItem("Mouse", -1, 25.50));
    }

    @Test
    void testNegativePriceThrowsException() {
        assertThrows(IllegalArgumentException.class, 
            () -> new OrderItem("Laptop", 1, -10.00));
    }

    @Test
    void testZeroPriceThrowsException() {
        assertThrows(IllegalArgumentException.class,
            () -> new OrderItem("Free Sample", 1, 0.0));
    }

    @Test
    void testNullNameThrowsException() {
        assertThrows(IllegalArgumentException.class,
            () -> new OrderItem(null, 1, 10.0));
    }

    @Test
    void testEmptyNameThrowsException() {
        assertThrows(IllegalArgumentException.class,
            () -> new OrderItem("", 1, 10.0));
    }

    @Test
    void testBlankNameThrowsException() {
        assertThrows(IllegalArgumentException.class,
            () -> new OrderItem("   ", 1, 10.0));
    }

    @Test
    void testVerySmallPrice() {
        OrderItem item = new OrderItem("Penny Item", 1, 0.01);
        assertEquals(0.01, item.getTotalPrice(), 0.001);
    }

    @Test
    void testVeryLargePrice() {
        OrderItem item = new OrderItem("Luxury Car", 1, 1000000.00);
        assertEquals(1000000.00, item.getTotalPrice(), 0.001);
    }

    @Test
    void testPriceWithManyDecimals() {
        OrderItem item = new OrderItem("Item", 3, 10.333333);
        assertEquals(30.999999, item.getTotalPrice(), 0.000001);
    }
}
