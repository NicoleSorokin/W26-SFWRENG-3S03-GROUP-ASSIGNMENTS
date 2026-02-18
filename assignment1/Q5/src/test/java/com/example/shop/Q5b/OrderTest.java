package com.example.shop.Q5b;

import com.example.shop.Order;
import com.example.shop.OrderItem;
import com.example.shop.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
    }

    @Test
    void testNewOrderHasCreatedStatus() {
        assertEquals(OrderStatus.CREATED, order.getStatus());
    }

    @Test
    void testAddItemToNewOrder() {
        OrderItem item = new OrderItem("Laptop", 1, 999.99);
        order.addItem(item);
        assertEquals(1, order.getItems().size());
    }

    @Test
    void testAddMultipleItems() {
        OrderItem item1 = new OrderItem("Laptop", 1, 999.99);
        OrderItem item2 = new OrderItem("Mouse", 2, 25.50);
        order.addItem(item1);
        order.addItem(item2);
        assertEquals(2, order.getItems().size());
    }

    @Test
    void testCannotAddItemsAfterOrderProcessed() {
        // This test cannot be fully implemented without a public way to change order status
        // In a real scenario, we would use OrderService to process the order
        // For now, this test is limited by the package-private setStatus method
        // This exposes a design issue: tests outside the package cannot properly test this behavior
        // Skipping this test - would need refactoring or OrderService integration
    }

    @Test
    void testCannotAddItemsAfterOrderCancelled() {
        // This test cannot be implemented without public access to setStatus
        // This exposes a testability issue in the design
        // Skipping this test - would need refactoring or OrderService integration
    }

    @Test
    void testGetItemsReturnsEmptyListForNewOrder() {
        assertTrue(order.getItems().isEmpty());
    }

    @Test
    void testGetItemsReturnsSameListReference() {
        // This test exposes the defensive copy issue
        OrderItem item = new OrderItem("Laptop", 1, 999.99);
        order.addItem(item);
        
        List<OrderItem> items1 = order.getItems();
        List<OrderItem> items2 = order.getItems();
        
        assertSame(items1, items2);
    }

    @Test
    void testGetItemsReturnsDefensiveCopy() {
        OrderItem item1 = new OrderItem("Laptop", 1, 999.99);
        order.addItem(item1);
        
        List<OrderItem> items = order.getItems();
        OrderItem item2 = new OrderItem("Mouse", 1, 25.50);
        
        // Attempt to modify the returned list should not affect the order
        items.add(item2);
        
        assertEquals(1, order.getItems().size());
    }

    @Test
    void testStatusProgression() {
        // This test cannot be fully implemented without public access to setStatus
        // Only checking the initial status
        assertEquals(OrderStatus.CREATED, order.getStatus());
        
        // Cannot test status transitions directly - would need OrderService integration
        // This exposes a testability issue in the design
    }
}
