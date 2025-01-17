package com.example.myproject;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class SummaryActivity extends AppCompatActivity {

    public TextView totalExpensesText, totalEarningsText, savingsText;
    public ListView expensesList;
    public ArrayList<String> expenseSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        // Initialize Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_summary);
        setSupportActionBar(toolbar);

        // Initialize UI components
        totalExpensesText = findViewById(R.id.total_expenses);
        totalEarningsText = findViewById(R.id.total_earnings);
        savingsText = findViewById(R.id.savings);
        expensesList = findViewById(R.id.expenses_list);

        // Get data from Intent
        double totalExpenses = getIntent().getDoubleExtra("totalExpenses", 0);
        double totalEarnings = getIntent().getDoubleExtra("totalEarnings", 0);
        expenseSummary = getIntent().getStringArrayListExtra("expenseSummary");

        if (expenseSummary == null) {
            expenseSummary = new ArrayList<>();
        }

        double savings = totalEarnings - totalExpenses;

        // Set text views
        totalExpensesText.setText("Total Expenses: $" + String.format("%.2f", totalExpenses));
        totalEarningsText.setText("Total Earnings: $" + String.format("%.2f", totalEarnings));
        savingsText.setText("Savings: $" + String.format("%.2f", savings));

        // Populate ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expenseSummary);
        expensesList.setAdapter(adapter);
    }
}

