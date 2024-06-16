package com.example.retailcloudassessmentproject.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retailcloudassessmentproject.Dao.CartDao
import com.example.retailcloudassessmentproject.Dao.ItemDao
import com.example.retailcloudassessmentproject.R
import com.example.retailcloudassessmentproject.adapter.ProductListATDR
import com.example.retailcloudassessmentproject.databinding.ActivityMainBinding
import com.example.retailcloudassessmentproject.manager.Utils
import com.example.retailcloudassessmentproject.model.Item
import com.example.retailcloudassessmentproject.viewmodel.MainViewModel
import com.example.retailcloudassessmentproject.viewmodel.MainViewModelFactory
import io.realm.Realm
import io.realm.RealmConfiguration
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private val productListATDR = ProductListATDR()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory = MainViewModelFactory(this)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        setupRecyclerView()
        observeViewModel()
        setUpListeners()
    }

    /**
     * Sets up the listeners for the UI components
     */
    private fun setUpListeners() {
        binding.cartIV.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
    }


    /**
     * Sets up the RecyclerView to display the list of products.
     */
    private fun setupRecyclerView() {
        binding.homeRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.homeRV.adapter = productListATDR
        productListATDR.setOnClickListeners(object : ProductListATDR.OnItemClickListeners{
            override fun onItemClick(position: Int, item: Item) {
                CartDao.addItemToCart(item)
                startActivity(Intent(this@MainActivity, CartActivity::class.java))
            }
        })
    }

    /**
     * Observes the ViewModel's LiveData properties to update the UI in response to data changes.
     */
    private fun observeViewModel() {
        viewModel.isLoading.observe(this, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) VISIBLE else GONE
        })

        viewModel.items.observe(this, Observer { items ->
            items?.let {
                productListATDR.setItems(ArrayList(it))
            }
        })

        viewModel.totalProductCount.observe(this, Observer { totalCount ->
            binding.cartItemCL.visibility = if (totalCount > 0) View.VISIBLE else View.GONE
            binding.itemCountTV.text = "Qty: "+totalCount
        })

        viewModel.totalAmount.observe(this, Observer { totalAmount ->
            binding.priceTV.text =
                getString(R.string.total_amount, Utils.toCurrencyString(totalAmount))
        })

        viewModel.totalTax.observe(this, Observer { totalTax ->
            val formattedTotalTax = String.format(Locale.ENGLISH, "%.2f", totalTax)
            binding.taxPercentTV.text = "Tax: " + formattedTotalTax
        })

        viewModel.totalAmountWithTax.observe(this, Observer { totalAmountWithTax ->
            binding.taxPriceTV.text =
                getString(R.string.total_inclu_tax, Utils.toCurrencyString(totalAmountWithTax))
        })


    }
}

