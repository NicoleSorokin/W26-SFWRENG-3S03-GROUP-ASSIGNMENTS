package com.example.shop.Q5b;

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
    void testProcessOrderWithValidPaymentAndStudent10Discount() {
        Order order = new Order();
        order.addItem(new OrderItem("Laptop", 1, 100.0));
        
        double total = orderService.processOrder(order, "STUDENT10", "card");
        
        // Subtotal: 100, Discount: 90, Tax: 18, Total: 108
        assertEquals(108.0, total, 0.001);
        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    void testProcessOrderWithValidPaymentAndBlackFridayDiscount() {
        Order order = new Order();
        order.addItem(new OrderItem("Laptop", 1, 100.0));
        
        double total = orderService.processOrder(order, "BLACKFRIDAY", "paypal");
        
        // Subtotal: 100, Discount: 70, Tax: 14, Total: 84
        assertEquals(84.0, total, 0.001);
        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    void testProcessOrderWithInvalidPaymentMethod() {
        Order order = new Order();
        order.addItem(new OrderItem("Laptop", 1, 100.0));
        
        double total = orderService.processOrder(order, null, "crypto");
        
        assertEquals(0.0, total, 0.001);
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void testProcessOrderWithNullPaymentMethod() {
        Order order = new Order();
        order.addItem(new OrderItem("Laptop", 1, 100.0));
        
        double total = orderService.processOrder(order, null, null);
        
        assertEquals(0.0, total, 0.001);
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void testProcessOrderWithUnknownPaymentMethodThrowsException() {
        Order order = new Order();
        order.addItem(new OrderItem("Laptop", 1, 100.0));
        
        assertThrows(UnsupportedOperationException.class,
            () -> orderService.processOrder(order, null, "bitcoin"));
    }

    @Test
    void testProcessEmptyOrder() {
        Order order = new Order();
        
        double total = orderService.processOrder(order, null, "card");
        
        // Empty order: Subtotal: 0, Tax: 0, Total: 0
        assertEquals(0.0, total, 0.001);
        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    void testProcessOrderWithMultipleItems() {
        Order order = new Order();
        order.addItem(new OrderItem("Laptop", 1, 1000.0));
        order.addItem(new OrderItem("Mouse", 2, 50.0));
        
        double total = orderService.processOrder(order, null, "card");
        
        // Subtotal: 1100, Tax: 220, Total: 1320
        assertEquals(1320.0, total, 0.001);
        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    void testProcessOrderWithMultipleItemsAndDiscount() {
        Order order = new Order();
        order.addItem(new OrderItem("Laptop", 1, 1000.0));
        order.addItem(new OrderItem("Mouse", 2, 50.0));
        
        double total = orderService.processOrder(order, "STUDENT10", "paypal");
        
        // Subtotal: 1100, Discounted: 990, Tax: 198, Total: 1188
        assertEquals(1188.0, total, 0.001);
        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    void testProcessOrderWithInvalidDiscountCodeThrowsException() {
        Order order = new Order();
        order.addItem(new OrderItem("Laptop", 1, 100.0));
        
        assertThrows(IllegalArgumentException.class,
            () -> orderService.processOrder(order, "INVALID", "card"));
    }

    @Test
    void testProcessOrderPaymentMethodCaseSensitivity() {
        Order order = new Order();
        order.addItem(new OrderItem("Laptop", 1, 100.0));
        
        double total1 = orderService.processOrder(order, null, "CARD");
        assertEquals(120.0, total1, 0.001);
        
        // Need new order as previous one is already paid
        Order order2 = new Order();
        order2.addItem(new OrderItem("Laptop", 1, 100.0));
        double total2 = orderService.processOrder(order2, null, "PayPal");
        assertEquals(120.0, total2, 0.001);
    }

    @Test
    void testProcessOrderDiscountCodeCaseSensitivity() {
        Order order1 = new Order();
        order1.addItem(new OrderItem("Laptop", 1, 100.0));
        double total1 = orderService.processOrder(order1, "student10", "card");
        assertEquals(108.0, total1, 0.001);
        
        Order order2 = new Order();
        order2.addItem(new OrderItem("Laptop", 1, 100.0));
        double total2 = orderService.processOrder(order2, "blackfriday", "card");
        assertEquals(84.0, total2, 0.001);
    }

    @Test
    void testProcessOrderWithEmptyDiscountCode() {
        Order order = new Order();
        order.addItem(new OrderItem("Laptop", 1, 100.0));
        
        double total = orderService.processOrder(order, "", "card");
        
        // Empty discount code should not apply discount
        assertEquals(120.0, total, 0.001);
        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    void testProcessOrderWithUnrecognizedDiscountCode() {
        Order order = new Order();
        order.addItem(new OrderItem("Laptop", 1, 100.0));
        
        double total = orderService.processOrder(order, "UNKNOWN", "card");
        
        // Unknown discount code should not apply discount
        assertEquals(120.0, total, 0.001);
        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    void testProcessOrderStatusChanges() {
        Order order = new Order();
        order.addItem(new OrderItem("Laptop", 1, 100.0));
        
        assertEquals(OrderStatus.CREATED, order.getStatus());
        
        orderService.processOrder(order, null, "card");
        
        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    void testProcessOrderWithEmptyPaymentMethodThrowsException() {
        Order order = new Order();
        order.addItem(new OrderItem("Laptop", 1, 100.0));
        
        assertThrows(UnsupportedOperationException.class,
            () -> orderService.processOrder(order, null, ""));
    }
}
