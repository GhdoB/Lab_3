package com.example.lab3;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {

    private EditText txtDisplay;
    private TextView txtPreviousCalculation;
    private StringBuilder currentInput = new StringBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });


         txtDisplay = findViewById(R.id.txtDisplay);
         txtPreviousCalculation = findViewById(R.id.txtPreviousCalculation);
         int [] numberButtonIds = {
                 R.id.btnZero, R.id.btnOne, R.id.btnTwo, R.id.btnThree, R.id.btnFour, R.id.btnFive,
                 R.id.btnSix, R.id.btnSeven, R.id.btnEight, R.id.btnNine
         };


         for (int id : numberButtonIds){
             Button btn = findViewById(id);
             btn.setOnClickListener(v -> appendToInput(btn.getText().toString()));
         }

        findViewById(R.id.btnAdd).setOnClickListener(v -> appendOperator("+"));
        findViewById(R.id.btnSubtract).setOnClickListener(v -> appendOperator("-"));
        findViewById(R.id.btnMultiply).setOnClickListener(v -> appendOperator("*"));
        findViewById(R.id.btnDivide).setOnClickListener(v -> appendOperator("/"));
        findViewById(R.id.btnPercent).setOnClickListener(v -> appendOperator("%"));
        findViewById(R.id.btnDecimal).setOnClickListener(v -> appendDecimalPoint());

        findViewById(R.id.btnCE).setOnClickListener(v -> {
            currentInput.setLength(0);
            txtDisplay.setText("0");
            txtPreviousCalculation.setText("");
        });

        findViewById(R.id.btnDEL).setOnClickListener(v -> {
            if (currentInput.length() > 0) {
                currentInput.deleteCharAt(currentInput.length() - 1);
                txtDisplay.setText(currentInput.length() > 0 ? currentInput.toString() : "0");
            }
        });

        findViewById(R.id.btnEquals).setOnClickListener(v -> evaluateExpression());

    }

    private void appendToInput(String value) {
        currentInput.append(value);
        txtDisplay.setText(currentInput.toString());
    }

    private void appendOperator(String operator) {
        if (currentInput.length() == 0) return;
        char lastChar = currentInput.charAt(currentInput.length() - 1);
        if ("+-*/%.".indexOf(lastChar) == -1) {
            currentInput.append(operator);
            txtDisplay.setText(currentInput.toString());
        }
    }

    private void appendDecimalPoint() {
        // prevent multiple '.' in a single number
        int i = currentInput.length() - 1;
        while (i >= 0 && Character.isDigit(currentInput.charAt(i))) i--;
        if (i < 0 || currentInput.charAt(i) != '.') {
            currentInput.append(".");
            txtDisplay.setText(currentInput.toString());
        }
    }

    private void evaluateExpression() {
        try {
            String expressionStr = currentInput.toString();

            // Replace % with /100 for percentage handling
            expressionStr = expressionStr.replaceAll("%", "/100");

            Expression expression = new ExpressionBuilder(expressionStr).build();
            double result = expression.evaluate();

            // Display results
            txtPreviousCalculation.setText(expressionStr);
            txtDisplay.setText(formatResult(result));
            currentInput = new StringBuilder(formatResult(result));
        } catch (Exception e) {
            txtDisplay.setText("Error");
        }
    }

    private String formatResult(double result) {
        if (result == (long) result) {
            return String.format("%d", (long) result);
        } else {
            return String.format("%s", result);
        }
    }

}