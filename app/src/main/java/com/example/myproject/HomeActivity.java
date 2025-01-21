package com.example.myproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize buttons and set click listeners
        Button btnTaxBreakdown = findViewById(R.id.btnTaxBreakdown);

        Button btnExpenseBreakdown = findViewById(R.id.btnExpenseBreakdown);

        TextView desTextView = findViewById(R.id.Des);

        btnTaxBreakdown.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                desTextView.setText("View a detailed breakdown of your taxes.");
            }
        });



        btnExpenseBreakdown.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                desTextView.setText("Analyze your expenses by category.");
            }
        });
        btnTaxBreakdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, TaxBreak.class);
                startActivity(intent);
            }
        });




        btnExpenseBreakdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ExpenseTracker.class);
                startActivity(intent);
            }
        });
    }
}
