package com.example.shop.Q5a;

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
    void testGetItemsReturnsDefensiveCopy() {
        OrderItem item1 = new OrderItem("Laptop", 1, 999.99);
        order.addItem(item1);
        
        List<OrderItem> items = order.getItems();
        OrderItem item2 = new OrderItem("Mouse", 1, 25.50);
        
        // Attempt to modify the returned list should not affect the order
        items.add(item2);
        
        assertEquals(1, order.getItems().size());
    }
}
