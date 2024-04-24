package com.example.prm_1

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.prm_1.R
import java.text.SimpleDateFormat
import java.util.Date

class ProductsAdapter(private val context: Context, private var productList: MutableList<Product>) : RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    class ProductViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.product_item, parent, false)) {
        private var productName: TextView = itemView.findViewById(R.id.productName)
        private var productCategory: TextView = itemView.findViewById(R.id.productCategory)
        private var productDate: TextView = itemView.findViewById(R.id.productDate)
        private var productQuantity: TextView = itemView.findViewById(R.id.productQuantity)

        fun bind(product: Product, clickListener: (Product) -> Unit) {
            productName.text = product.name
            productCategory.text = product.category
            productDate.text = SimpleDateFormat("dd/MM/yyyy").format(Date(product.expirationDate))
            productQuantity.text = product.quantity?.toString() ?: "N/A"
            itemView.setOnClickListener { clickListener(product) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProductViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return productList.size;
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productList[position]) { product ->
            showEditMenu(product, position)
        }
    }

    private fun showEditMenu(product: Product, position: Int) {
        AlertDialog.Builder(context).apply {
            setTitle("Select Option")
            setItems(arrayOf("Edit", "Delete")) { dialog, which ->
                when (which) {
                    0 -> {
                        val intent = Intent(context, AddEditProductActivity::class.java)
                        intent.putExtra("EDIT_PRODUCT", product)
                        context.startActivity(intent)
                    }
                    1 -> {
                        removeProduct(position)
                    }
                }
            }
            show()
        }
    }

    fun addProduct(product: Product) {
        productList.add(product)
        notifyItemInserted(productList.size - 1)
    }

    fun removeProduct(position: Int) {
        if (position >= 0 && position < productList.size) {
            productList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}
