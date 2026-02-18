package com.example.shop.Q5b;

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
    void testNullPaymentMethodReturnsFalse() {
        assertFalse(validator.isPaymentMethodValid(null));
    }

    @Test
    void testCardPaymentLowercase() {
        assertTrue(validator.isPaymentMethodValid("card"));
    }

    @Test
    void testCardPaymentUppercase() {
        assertTrue(validator.isPaymentMethodValid("CARD"));
    }

    @Test
    void testCardPaymentMixedCase() {
        assertTrue(validator.isPaymentMethodValid("CaRd"));
    }

    @Test
    void testPaypalPaymentLowercase() {
        assertTrue(validator.isPaymentMethodValid("paypal"));
    }

    @Test
    void testPaypalPaymentUppercase() {
        assertTrue(validator.isPaymentMethodValid("PAYPAL"));
    }

    @Test
    void testPaypalPaymentMixedCase() {
        assertTrue(validator.isPaymentMethodValid("PayPal"));
    }

    @Test
    void testCryptoPaymentReturnsFalse() {
        assertFalse(validator.isPaymentMethodValid("crypto"));
    }

    @Test
    void testCryptoPaymentUppercaseReturnsFalse() {
        assertFalse(validator.isPaymentMethodValid("CRYPTO"));
    }

    @Test
    void testCryptoPaymentMixedCaseReturnsFalse() {
        assertFalse(validator.isPaymentMethodValid("Crypto"));
    }

    @Test
    void testUnknownPaymentMethodReturnsFalse() {
        assertFalse(validator.isPaymentMethodValid("bitcoin"));
    }

    @Test
    void testEmptyStringReturnsFalse() {
        assertFalse(validator.isPaymentMethodValid(""));
    }

    @Test
    void testBlankStringReturnsFalse() {
        assertFalse(validator.isPaymentMethodValid("   "));
    }

    @Test
    void testRandomPaymentMethodReturnsFalse() {
        assertFalse(validator.isPaymentMethodValid("cash"));
    }

    @Test
    void testPaymentMethodWithSpacesReturnsFalse() {
        assertFalse(validator.isPaymentMethodValid("card payment"));
    }

    @Test
    void testPartiallyMatchingPaymentMethodReturnsFalse() {
        assertFalse(validator.isPaymentMethodValid("cards"));
    }

    @Test
    void testPaymentMethodWithSpecialCharactersReturnsFalse() {
        assertFalse(validator.isPaymentMethodValid("card@123"));
    }
}
