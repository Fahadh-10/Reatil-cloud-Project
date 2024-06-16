package com.example.retailcloudassessmentproject.activity

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retailcloudassessmentproject.Dao.CartDao
import com.example.retailcloudassessmentproject.adapter.CartListATDR
import com.example.retailcloudassessmentproject.databinding.ActivityCartBinding
import com.example.retailcloudassessmentproject.manager.Utils
import com.example.retailcloudassessmentproject.model.CartItem
import com.example.retailcloudassessmentproject.viewmodel.CartViewModel
import java.util.Locale

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private val cartListATDR = CartListATDR()
    private lateinit var cartViewModel: CartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel()
        setupRecyclerView()
        observeViewModel()
        loadCartItems()
        setUpListeners()
    }

    /**
     * Sets up the ViewModel for the activity. It is used to manage the data for the cart
     */
    private fun setupViewModel() {
        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)
    }

    /**
     * Observes the ViewModel's LiveData properties to update the UI in response to data changes.
     */
    private fun observeViewModel() {
        cartViewModel.cartItems.observe(this, Observer { cartItems ->
            if (!cartItems.isNullOrEmpty()) {
                cartItems?.let {
                    cartListATDR.setItems(it)
                    updateUIWithCartData(it)
                }
            } else {
                binding.cartListRV.visibility = GONE
                binding.cartTotalCL.visibility = GONE
                binding.cartEmptyCL.visibility = VISIBLE
            }
        })

        cartViewModel.isLoading.observe(this, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) VISIBLE else GONE
        })
    }

    /**
     * Loads the cart items from the ViewModel to fetch the data.
     */
    private fun loadCartItems() {
        cartViewModel.fetchCartItems()
    }

    /**
     * Sets up the listeners for the UI components
     */
    private fun setUpListeners() {
        binding.backIV.setOnClickListener {
            finish()
        }
    }

    /**
     * Sets up the RecyclerView to display the list of cart items.
     */
    private fun setupRecyclerView() {
        binding.cartListRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.cartListRV.adapter = cartListATDR
    }

    /**
     * Updates the UI with the data from the cart.
     * @param cartItems The list of cart items to be displayed.
     */
    private fun updateUIWithCartData(cartItems: List<CartItem>) {
        val totalPrice = cartViewModel.calculateTotalAmount(cartItems)
        binding.youPayPriceTV.text = "Total Price: " + Utils.toCurrencyString(totalPrice)
        val totalQuantity = cartViewModel.calculateTotalQuantity(cartItems)
        binding.totalItemsTV.text = "Total Quantity: $totalQuantity"
        val totalTax = cartViewModel.calculateTotalTaxAmount(cartItems)
        val formattedTotalTax = String.format(Locale.ENGLISH, "%.2f", totalTax)
        binding.totalTaxTV.text = "Total Tax Percent: $formattedTotalTax %"
        val totalIncludingTax = cartViewModel.calculateTotalWithTax(cartItems)
        val formattedTotal = String.format(Locale.ENGLISH, "%.2f", totalIncludingTax)
        binding.cartContinuePaymentBTN.text = "Total Incl. Tax:\n${Utils.toCurrencyString(formattedTotal.toDouble())}"
    }
}

