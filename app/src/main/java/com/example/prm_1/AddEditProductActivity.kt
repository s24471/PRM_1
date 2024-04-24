package com.example.prm_1

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class AddEditProductActivity : AppCompatActivity() {

    private lateinit var productName: EditText
    private lateinit var productCategory: Spinner
    private lateinit var productQuantity: EditText
    private lateinit var datePickerButton: Button
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    private var calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_product)

        productName = findViewById(R.id.editProductName)
        productCategory = findViewById(R.id.spinnerCategory)
        productQuantity = findViewById(R.id.editProductQuantity)
        datePickerButton = findViewById(R.id.datePickerButton)
        saveButton = findViewById(R.id.buttonSaveProduct)
        cancelButton = findViewById(R.id.buttonCancel)

        setupCategorySpinner()
        setupDatePicker()
        setupButtonListeners()
    }

    private fun setupCategorySpinner() {
        val categories = arrayOf("Food", "Medicine", "Household", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        productCategory.adapter = adapter
    }

    private fun setupDatePicker() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        datePickerButton.setOnClickListener {
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                datePickerButton.text = dateFormat.format(calendar.time)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun setupButtonListeners() {
        saveButton.setOnClickListener {
            if (validateInputs()) {
                val intent = Intent().apply {
                    putExtra("PRODUCT_NAME", productName.text.toString())
                    putExtra("PRODUCT_CATEGORY", productCategory.selectedItem.toString())
                    putExtra("PRODUCT_QUANTITY", productQuantity.text.toString().toInt())
                    putExtra("PRODUCT_EXPIRATION", calendar.timeInMillis)
                }
                setResult(RESULT_OK, intent)
                finish()
            }
        }

        cancelButton.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    private fun validateInputs(): Boolean {
        if (productName.text.isBlank()) {
            productName.error = "Product name cannot be empty"
            return false
        }

        if (productQuantity.text.isNotEmpty() && productQuantity.text.toString().toIntOrNull() == null) {
            productQuantity.error = "Quantity must be a number"
            return false
        }

        if (calendar.timeInMillis < System.currentTimeMillis()) {
            datePickerButton.error = "Expiration date cannot be in the past"
            return false
        }

        return true
    }
}