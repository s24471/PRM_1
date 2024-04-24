package com.example.prm_1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var addButton: Button
    private lateinit var summaryText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        productsAdapter = ProductsAdapter(this, loadSampleData().toMutableList())
        recyclerView.adapter = productsAdapter

        addButton = findViewById(R.id.addButton)
        summaryText = findViewById(R.id.summaryText)
        updateSummary()

        addButton.setOnClickListener {
            val intent = Intent(this, AddEditProductActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadSampleData(): List<Product> {
        // Example sample data
        return listOf(
            Product("Milk", "Food", System.currentTimeMillis() + 86400000, 2, "Valid"),
            Product("Aspirin", "Medicine", System.currentTimeMillis() + 172800000, 1, "Valid")
        )
    }

    fun updateSummary() {
        summaryText.text = "Total products: ${productsAdapter.itemCount}"
    }

    private fun showAddProductDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_add_product, null)
        val productName = dialogLayout.findViewById<EditText>(R.id.editProductName)
        val productCategory = dialogLayout.findViewById<EditText>(R.id.editProductCategory)
        val productExpiration = dialogLayout.findViewById<EditText>(R.id.editProductExpiration)
        val productQuantity = dialogLayout.findViewById<EditText>(R.id.editProductQuantity)

        with(builder) {
            setView(dialogLayout)
            setTitle("Add New Product")
            setPositiveButton("Add") { dialog, _ ->
                val name = productName.text.toString()
                val category = productCategory.text.toString()
                val expirationStr = productExpiration.text.toString()
                val quantity = productQuantity.text.toString().toIntOrNull()
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                val expirationDate = sdf.parse(expirationStr)?.time ?: System.currentTimeMillis()

                val newProduct = Product(name, category, expirationDate, quantity, "Valid")
                productsAdapter.addProduct(newProduct)
                updateSummary()
                dialog.dismiss()
            }
            setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            show()
        }
    }

}