package com.rd.calculator;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private final CalculatorEngine engine = new CalculatorEngine();

    @Test
    public void addition_isCorrect() {
        assertEquals(4.0, engine.calculate(2, 2, "+"), 0.0001);
    }

    @Test
    public void divisionByZero_returnsNaN() {
        assertTrue(Double.isNaN(engine.calculate(5, 0, "÷")));
    }

    @Test
    public void percentage_isCorrect() {
        assertEquals(20.0, engine.calculate(200, 10, "%"), 0.0001);
    }
}