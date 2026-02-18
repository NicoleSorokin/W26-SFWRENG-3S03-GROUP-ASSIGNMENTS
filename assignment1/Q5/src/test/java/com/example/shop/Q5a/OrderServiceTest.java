package com.example.shop.Q5a;

import com.example.shop.Order;
import com.example.shop.OrderItem;
import com.example.shop.OrderService;
import com.example.shop.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService();
    }

    @Test
    void testProcessOrderWithValidPaymentAndNoDiscount() {
        Order order = new Order();
        order.addItem(new OrderItem("Laptop", 1, 100.0));
        
        double total = orderService.processOrder(order, null, "card");
        
        // Subtotal: 100, Discount: none (100), Tax: 20, Total: 120
        assertEquals(120.0, total, 0.001);
        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    void testProcessOrderWithValidPaymentAndDiscount() {
        Order order = new Order();
        order.addItem(new OrderItem("Laptop", 1, 100.0));
        
        double total = orderService.processOrder(order, "STUDENT10", "card");
        
        // Subtotal: 100, Discount: 90, Tax: 18, Total: 108
        assertEquals(108.0, total, 0.001);
        assertEquals(OrderStatus.PAID, order.getStatus());
    }
}
