package com.rd.calculator;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // 1. Declare View variables
    private TextView textExpression;
    private TextView textResult;

    // 2. Declare Model variable
    private CalculatorEngine engine;

    // 3. Variables to keep track of calculator memory state
    private double firstOperand = 0;
    private String activeOperator = "";
    private boolean isNewInput = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Links this Java class to our XML file
        setContentView(R.layout.activity_main);

        // Initialize our Model class
        engine = new CalculatorEngine();

        // Connect view elements to XML layout IDs manually
        textExpression = findViewById(R.id.textExpression);
        textResult = findViewById(R.id.textResult);

        // Map number button listeners using a custom single helper method
        int[] numberButtonIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btnDot
        };

        for (int id : numberButtonIds) {
            findViewById(id).setOnClickListener(v -> {
                Button button = (Button) v;
                String digit = button.getText().toString();
                handleNumberInput(digit);
            });
        }

        // Map operational functional action buttons individually
        findViewById(R.id.btnAdd).setOnClickListener(v -> handleOperatorInput("+"));
        findViewById(R.id.btnSubtract).setOnClickListener(v -> handleOperatorInput("-"));
        findViewById(R.id.btnMultiply).setOnClickListener(v -> handleOperatorInput("×"));
        findViewById(R.id.btnDivide).setOnClickListener(v -> handleOperatorInput("÷"));
        findViewById(R.id.btnPercent).setOnClickListener(v -> handleOperatorInput("%"));

        findViewById(R.id.btnEqual).setOnClickListener(v -> handleEqualInput());
        findViewById(R.id.btnC).setOnClickListener(v -> handleClearInput());
        findViewById(R.id.btnBackspace).setOnClickListener(v -> handleBackspaceInput());
        findViewById(R.id.btnToggle).setOnClickListener(v -> handleToggleInput());
    }

    // --- CONTROLLER LOGIC HELPER METHODS ---

    private void handleNumberInput(String digit) {
        if (isNewInput || textResult.getText().toString().equals("Error")) {
            textResult.setText(digit.equals(".") ? "0." : digit);
            isNewInput = false;
        } else {
            String current = textResult.getText().toString();
            if (digit.equals(".") && current.contains(".")) return; // Ignore multiple dots
            textResult.setText(current + digit);
        }
    }

    private void handleOperatorInput(String op) {
        if (textResult.getText().toString().equals("Error")) {
            return;
        }
        if (!activeOperator.isEmpty() && !isNewInput) {
            handleEqualInput(); // Calculate current step if user chain inputs operators
            if (textResult.getText().toString().equals("Error")) {
                return;
            }
        }
        try {
            firstOperand = Double.parseDouble(textResult.getText().toString());
        } catch (NumberFormatException e) {
            textResult.setText("Error");
            isNewInput = true;
            return;
        }
        activeOperator = op;
        textExpression.setText(formatNumberDisplay(firstOperand) + " " + op);
        isNewInput = true;
    }

    private void handleEqualInput() {
        if (activeOperator.isEmpty() || isNewInput || textResult.getText().toString().equals("Error")) return;

        double secondOperand;
        try {
            secondOperand = Double.parseDouble(textResult.getText().toString());
        } catch (NumberFormatException e) {
            textResult.setText("Error");
            isNewInput = true;
            return;
        }

        double output = engine.calculate(firstOperand, secondOperand, activeOperator);

        // Build top display string expression layout labels
        if (activeOperator.equals("%")) {
            textExpression.setText(formatNumberDisplay(firstOperand) + " × " + formatNumberDisplay(secondOperand) + "% =");
        } else {
            textExpression.setText(textExpression.getText().toString() + " " + formatNumberDisplay(secondOperand) + " =");
        }

        if (Double.isNaN(output) || Double.isInfinite(output)) {
            textResult.setText("Error");
        } else {
            textResult.setText(formatNumberDisplay(output));
        }

        activeOperator = "";
        isNewInput = true;
    }

    private void handleClearInput() {
        textExpression.setText("");
        textResult.setText("0");
        firstOperand = 0;
        activeOperator = "";
        isNewInput = true;
    }

    private void handleBackspaceInput() {
        if (isNewInput || textResult.getText().toString().equals("Error")) {
            textResult.setText("0");
            isNewInput = true;
            return;
        }

        String current = textResult.getText().toString();
        if (current.length() > 1) {
            textResult.setText(current.substring(0, current.length() - 1));
        } else {
            textResult.setText("0");
            isNewInput = true;
        }
    }

    private void handleToggleInput() {
        if (textResult.getText().toString().equals("Error")) return;

        try {
            double val = Double.parseDouble(textResult.getText().toString()) * -1;
            textResult.setText(formatNumberDisplay(val));
        } catch (NumberFormatException e) {
            textResult.setText("Error");
            isNewInput = true;
        }
    }

    // Helper method to hide .0 if the number is a round integer
    private String formatNumberDisplay(double val) {
        if (val == (long) val) {
            return String.valueOf((long) val);
        } else {
            return String.valueOf(val);
        }
    }
}