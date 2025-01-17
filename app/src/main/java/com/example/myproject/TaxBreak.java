package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TaxBreak extends AppCompatActivity {

    // UI Components
    private EditText salaryInput, childExpenseInput, childAgeInput, loanPrincipalInput;
    private RadioGroup childRadioGroup, studentLoanRadioGroup;
    private Spinner provinceSpinner;
    private Button calculateButton;
    private TextView resultText;

    // Variables to store input values
    private boolean hasChild, hasStudentLoan;
    private String selectedProvince;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tax_break);


        initializeUI();


        setupProvinceSpinner();


        setupRadioGroupListeners();


        calculateButton.setOnClickListener(v -> calculateTax());

        ImageButton backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaxBreak.this, HomeActivity.class);
                startActivity(intent);// This handles the back navigation
            }
        });


    }



    private void initializeUI() {
        salaryInput = findViewById(R.id.salaryInput);
        childExpenseInput = findViewById(R.id.childExpenseInput);
        childAgeInput = findViewById(R.id.childAgeInput);
        loanPrincipalInput = findViewById(R.id.loanPrincipalInput);
        childRadioGroup = findViewById(R.id.childRadioGroup);
        studentLoanRadioGroup = findViewById(R.id.studentLoanRadioGroup);
        provinceSpinner = findViewById(R.id.provinceSpinner);
        calculateButton = findViewById(R.id.calculateButton);
        resultText = findViewById(R.id.resultText);

        childExpenseInput.setVisibility(View.GONE);
        childAgeInput.setVisibility(View.GONE);
        loanPrincipalInput.setVisibility(View.GONE);
    }


    private void setupProvinceSpinner() {
        String[] provinces = {"Select Province", "British Columbia", "Manitoba", "Quebec", "Ontario", "Alberta"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinces);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provinceSpinner.setAdapter(adapter);

        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedProvince = provinces[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedProvince = "";
            }
        });
    }


    private void setupRadioGroupListeners() {
        childRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.childYesRadio) {
                hasChild = true;
                childExpenseInput.setVisibility(View.VISIBLE);
                childAgeInput.setVisibility(View.VISIBLE);
            } else {
                hasChild = false;
                childExpenseInput.setVisibility(View.GONE);
                childAgeInput.setVisibility(View.GONE);
            }
        });

        studentLoanRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.studentLoanYesRadio) {
                hasStudentLoan = true;
                loanPrincipalInput.setVisibility(View.VISIBLE);
            } else {
                hasStudentLoan = false;
                loanPrincipalInput.setVisibility(View.GONE);
            }
        });
    }


    private void calculateTax() {
        String salaryStr = salaryInput.getText().toString().trim();
        if (TextUtils.isEmpty(salaryStr)) {
            salaryInput.setError("Please enter your salary");
            return;
        }
        int salary = Integer.parseInt(salaryStr);

        int childExpense = 0, childAge = 0;
        if (hasChild) {
            String childExpenseStr = childExpenseInput.getText().toString().trim();
            String childAgeStr = childAgeInput.getText().toString().trim();
            if (!TextUtils.isEmpty(childExpenseStr) && !TextUtils.isEmpty(childAgeStr)) {
                childExpense = Integer.parseInt(childExpenseStr);
                childAge = Integer.parseInt(childAgeStr);
            }
        }

        double loanPrincipal = 0;
        if (hasStudentLoan) {
            String loanPrincipalStr = loanPrincipalInput.getText().toString().trim();
            if (!TextUtils.isEmpty(loanPrincipalStr)) {
                loanPrincipal = Double.parseDouble(loanPrincipalStr);
            }
        }

        if (TextUtils.isEmpty(selectedProvince) || selectedProvince.equals("Select Province")) {
            Toast.makeText(this, "Please select a province", Toast.LENGTH_SHORT).show();
            return;
        }

        double rrspDeduction = calculateRRSP(salary);
        int childcareDeduction = calculateChildcareDeduction(childExpense, childAge);
        double studentLoanDeduction = calculateStudentLoanDeduction(loanPrincipal, selectedProvince);
        double totalDeductions = rrspDeduction + childcareDeduction + studentLoanDeduction;

        double taxableIncome = salary - totalDeductions;
        double federalTax = calculateFederalTax(taxableIncome);
        double provincialTax = calculateProvincialTax(taxableIncome, selectedProvince);
        double totalTax = federalTax + provincialTax;

        String result = String.format("Taxable Income: $%.2f\nFederal Tax: $%.2f\nProvincial Tax: $%.2f\nTotal Tax: $%.2f",
                taxableIncome, federalTax, provincialTax, totalTax);
        resultText.setText(result);
    }

    private double calculateRRSP(int income) {
        return Math.min(income * 0.18, 31560);
    }

    private int calculateChildcareDeduction(int expense, int age) {
        if (age < 7) return Math.min(expense, 8000);
        if (age >= 7 && age < 17) return Math.min(expense, 5000);
        return 0;
    }

    private double calculateStudentLoanDeduction(double principal, String province) {
        if (province.equals("British Columbia") || province.equals("Manitoba")) return 0;
        if (province.equals("Quebec")) return principal * 0.065;
        if (province.equals("Ontario")) return principal * 0.075;
        if (province.equals("Alberta")) return principal * 0.08;
        return 0;
    }

    private double calculateFederalTax(double income) {
        double tax = 0;
        if (income > 246752) tax += (income - 246752) * 0.33;
        if (income > 173205) tax += (income - 173205) * 0.2932;
        if (income > 111733) tax += (income - 111733) * 0.26;
        if (income > 55867) tax += (income - 55867) * 0.205;
        tax += Math.min(income, 55867) * 0.15;
        return tax;
    }

    private double calculateProvincialTax(double income, String province) {
        switch (province) {
            case "British Columbia": return income * 0.05;
            case "Manitoba": return income * 0.10;
            case "Quebec": return income * 0.15;
            case "Ontario": return income * 0.11;
            case "Alberta": return income * 0.08;
            default: return 0;
        }
    }


}