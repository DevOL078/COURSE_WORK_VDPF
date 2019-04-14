package ru.hse.app.selection.functional;

import org.junit.Test;

import static org.junit.Assert.*;

public class FunctionTest {

    @Test
    public void calculateTest1() {
        String expression = "n+1";
        Function function = new Function(expression);
        assertEquals(2, function.calculate(1));
        assertEquals(4, function.calculate(3));
        assertEquals(21, function.calculate(20));
        assertEquals(101, function.calculate(100));
        assertEquals(501, function.calculate(500));
    }

    @Test
    public void calculateTest2() {
        String expression = "2*n-1";
        Function function = new Function(expression);
        assertEquals(1, function.calculate(1));
        assertEquals(5, function.calculate(3));
        assertEquals(39, function.calculate(20));
        assertEquals(199, function.calculate(100));
        assertEquals(999, function.calculate(500));
    }

    @Test
    public void calculateTest3() {
        String expression = "n^2+3*n+5";
        Function function = new Function(expression);
        assertEquals(func3(1), function.calculate(1));
        assertEquals(func3(3), function.calculate(3));
        assertEquals(func3(20), function.calculate(20));
        assertEquals(func3(100), function.calculate(100));
        assertEquals(func3(500), function.calculate(500));
    }

    private int func3(int value) {
        return value*value + 3*value + 5;
    }

    @Test
    public void calculateTest4() {
        String expression = "(n+5)*(n-3)+100*n";
        Function function = new Function(expression);
        assertEquals(func4(1), function.calculate(1));
        assertEquals(func4(3), function.calculate(3));
        assertEquals(func4(20), function.calculate(20));
        assertEquals(func4(100), function.calculate(100));
        assertEquals(func4(500), function.calculate(500));
    }

    private int func4(int value) {
        return (value + 5)*(value-3) + 100*value;
    }

    @Test
    public void calculateTest5() {
        String expression = "100*n+20";
        Function function = new Function(expression);
        assertEquals(func5(1), function.calculate(1));
        assertEquals(func5(3), function.calculate(3));
        assertEquals(func5(20), function.calculate(20));
        assertEquals(func5(100), function.calculate(100));
        assertEquals(func5(500), function.calculate(500));
    }

    private int func5(int value) {
        return 100*value+20;
    }
}