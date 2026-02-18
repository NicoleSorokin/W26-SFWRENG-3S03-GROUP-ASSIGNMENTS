import org.junit.Test;
import static org.junit.Assert.*;

public class CalculatorDivideTest {

    private Calculator calculator = new Calculator();

    @Test
    public void divideTwoPositiveNumbers() {
        double result = calculator.divide(12, 6);
        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void divideByZeroShouldThrowException() {
        assertThrows(ArithmeticException.class, () -> {
            calculator.divide(1, 0);
        });
    }
}
