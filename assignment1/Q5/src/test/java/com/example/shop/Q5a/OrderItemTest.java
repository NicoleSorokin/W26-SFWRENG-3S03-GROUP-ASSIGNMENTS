package com.example.shop.Q5a;

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
    void testNullNameThrowsException() {
        assertThrows(IllegalArgumentException.class,
            () -> new OrderItem(null, 1, 10.0));
    }
}
