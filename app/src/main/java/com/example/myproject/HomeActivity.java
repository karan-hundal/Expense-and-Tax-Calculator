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
        Button btnExpenseReports = findViewById(R.id.btnExpenseReports);
        Button btnExpenseBreakdown = findViewById(R.id.btnExpenseBreakdown);

        TextView desTextView = findViewById(R.id.Des);

        btnTaxBreakdown.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                desTextView.setText("View a detailed breakdown of your taxes.");
            }
        });

        btnExpenseReports.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                desTextView.setText("Access your expense reports, including earnings and savings.");
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

        btnExpenseReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,SummaryActivity.class);
                startActivity(intent);
                double totalExpenses = getSharedPreferences("ExpenseTrackerPrefs", MODE_PRIVATE).getFloat("totalExpenses", 0);
                double totalEarnings = getSharedPreferences("ExpenseTrackerPrefs", MODE_PRIVATE).getFloat("totalEarnings", 0);
                double totalTax = getSharedPreferences("TaxBreakPrefs", MODE_PRIVATE).getFloat("totalTax", 0);

                intent.putExtra("totalExpenses", totalExpenses);
                intent.putExtra("totalEarnings", totalEarnings);
                intent.putExtra("taxAmount", totalTax);

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