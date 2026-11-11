package com.rd.calculator;

public class CalculatorEngine {
    // A simple method that accepts input data and outputs a calculation result
    public double calculate(double num1, double num2, String operator) {
        switch (operator) {
            case "+": return num1 + num2;
            case "-": return num1 - num2;
            case "×": return num1 * num2;
            case "÷": return num2 == 0 ? Double.NaN : num1 / num2;
            case "%": return num1 * (num2 / 100.0); // E.g. 10% of 200
            default: return 0;
        }
    }
}
