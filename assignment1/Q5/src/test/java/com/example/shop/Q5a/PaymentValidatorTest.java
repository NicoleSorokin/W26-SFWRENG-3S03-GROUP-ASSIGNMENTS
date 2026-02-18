package com.example.shop.Q5a;

import com.example.shop.PaymentValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentValidatorTest {

    private PaymentValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PaymentValidator();
    }

    @Test
    void testCardPaymentValid() {
        assertTrue(validator.isPaymentMethodValid("card"));
    }

    @Test
    void testUnknownPaymentMethodReturnsFalse() {
        assertFalse(validator.isPaymentMethodValid("bitcoin"));
    }
}
