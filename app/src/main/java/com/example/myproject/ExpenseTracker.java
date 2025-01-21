package com.example.myproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ExpenseTracker extends AppCompatActivity {


    private EditText categoryInput, expenseInput, sourceInput, earningInput;
    private Button addExpenseBtn, addEarningBtn;
    private ListView transactionList;
    private ImageButton backButton;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> transactions;
    private TransactionDatabaseHelper transactionDatabaseHelper;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_tracker);


        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        username = sharedPreferences.getString("username", null);

        if (username == null) {
            // If no username is found, redirect to the login screen
            Intent loginIntent = new Intent(ExpenseTracker.this, MainActivity.class);
            startActivity(loginIntent);
            finish();
        } else {

            transactionDatabaseHelper = new TransactionDatabaseHelper(this);
            initializeUI();
            loadUserTransactions();
            setButtonListeners();
        }
    }

    private void initializeUI() {
        categoryInput = findViewById(R.id.category_input);
        expenseInput = findViewById(R.id.expense_input);
        sourceInput = findViewById(R.id.source_input);
        earningInput = findViewById(R.id.earning_input);
        addExpenseBtn = findViewById(R.id.add_expense_btn);
        addEarningBtn = findViewById(R.id.add_earning_btn);
        transactionList = findViewById(R.id.transaction_list);
        backButton = findViewById(R.id.back_button);
    }

    private void loadUserTransactions() {
        transactions = transactionDatabaseHelper.getTransactionsByUser(username);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, transactions);
        transactionList.setAdapter(adapter);
    }

    private void setButtonListeners() {

        addExpenseBtn.setOnClickListener(v -> handleTransaction("Expense", categoryInput, expenseInput));


        addEarningBtn.setOnClickListener(v -> handleTransaction("Earning", sourceInput, earningInput));





        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void handleTransaction(String type, EditText inputField1, EditText inputField2) {
        String field1 = inputField1.getText().toString();
        String field2 = inputField2.getText().toString();

        if (validateInputs(field1, field2)) {
            double amount = Double.parseDouble(field2);
            boolean success = transactionDatabaseHelper.insertTransaction(username, type, field1, amount);

            if (success) {
                transactions.add(type + ": " + field1 + " - $" + String.format("%.2f", amount));
                adapter.notifyDataSetChanged();
                clearInputs(inputField1, inputField2);
            } else {
                showToast("Failed to save " + type.toLowerCase());
            }
        } else {
            showToast("Please enter valid details");
        }
    }

    private boolean validateInputs(String field1, String field2) {
        return !field1.isEmpty() && !field2.isEmpty() && isNumeric(field2);
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void clearInputs(EditText inputField1, EditText inputField2) {
        inputField1.setText("");
        inputField2.setText("");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}



