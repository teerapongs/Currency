package com.example.currency.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.currency.R
import com.example.currency.base.BaseActivity
import com.example.currency.databinding.ActivityMainBinding
import com.example.currency.databinding.LayoutConvertCurrencyBinding
import com.example.currency.databinding.LayoutDetailCurrencyBinding
import com.example.currency.extensions.navigate
import com.example.currency.extensions.onTextChanged
import com.example.currency.model.Currency
import com.example.currency.ui.history.HistoryActivity

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var detailBinding: LayoutDetailCurrencyBinding
    private lateinit var convertBinding: LayoutConvertCurrencyBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var handler: Handler
    private val data: MutableList<Currency?> = mutableListOf()
    private var dataMap: Map<String?,Currency?> = mapOf()
    private var currency: String = "USD"
    private var rateCurrency: String = "0.0"
    private var isDataValid: Boolean = false

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            mainViewModel.getCurrency()
            this@MainActivity.handler.postDelayed(this, 1000*60)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        detailBinding = binding.layoutDetailCurrency
        convertBinding = binding.layoutConvertCurrency
        setContentView(binding.root)
        setupView()
        setupObserver()
        setupSpinner()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()
        handler.post(runnable)
    }

    private fun setupView() {
        handler = Handler(Looper.getMainLooper())
        mainViewModel = ViewModelProvider(this, MainViewModelFactory())[MainViewModel::class.java]
        binding.btnHistory.setOnClickListener {
            navigate<HistoryActivity> {}
        }
        convertBinding.currencyEditText.onTextChanged {
            rateCurrency = it
            mainViewModel.convertCurrencyChanged(it)
        }
    }

    private fun setupSpinner() {
        val items = listOf(getString(R.string.usd), getString(R.string.gbp), getString(R.string.eur))
        val spinnerAdapter = ArrayAdapter(this, R.layout.spinner_items, items)
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_items_currency)
        convertBinding.currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                currency = items[p2]
                mainViewModel.convertCurrencyChanged(rateCurrency)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        convertBinding.currencySpinner.adapter = spinnerAdapter
    }

    @SuppressLint("SetTextI18n")
    private fun setupObserver() {
        mainViewModel.getCurrencySuccess.observe(this@MainActivity, Observer { resultResponse ->
            val result = resultResponse ?: return@Observer
            if (result.error?.isNotEmpty() == true || result.errorInt?.toString()?.isNotEmpty() == true) {
                val error = result.error ?: result.errorInt?.let { getString(it) }
                uiShowError(error)
                return@Observer
            }
            data.add(result.success)
            data.sortByDescending { it?.time?.updated }
            dataMap = data.associateBy { it?.time?.updated }
            userData.setHistoryCurrencyMap(dataMap)
            updateCurrency()
            if (isDataValid) {
                convertToBTC()
            }
        })

        mainViewModel.convertForm.observe(this@MainActivity, Observer {
            val state = it ?: return@Observer
            if (state.currencyError != null) {
                convertBinding.currencyEditText.error = getString(state.currencyError)
            }

            isDataValid = state.isDataValid
            if (state.isDataValid) {
                convertToBTC()
            }
        })

        mainViewModel.convertToBTC.observe(this@MainActivity, Observer {
            val data = it ?: return@Observer
            convertBinding.txtBtc.text = "$data ${getString(R.string.btc)}"
        })
    }

    private fun updateCurrency() {
        binding.txtChartName.text = data.first()?.chartName ?: ""
        detailBinding.txtUsd.text = data.first()?.bpi?.USD?.rate ?: "0.0"
        detailBinding.txtGbp.text = data.first()?.bpi?.GBP?.rate ?: "0.0"
        detailBinding.txtEur.text = data.first()?.bpi?.EUR?.rate ?: "0.0"
    }

    private fun convertToBTC() {
        val rate = getRate(currency)
        mainViewModel.convertToBTC(rate, rateCurrency)
    }

    private fun getRate(currency: String): Float {
        if (data.isNotEmpty()) {
            val rate = when (currency) {
                getString(R.string.usd) -> data.first()?.bpi?.USD?.rate_float
                getString(R.string.gbp) -> data.first()?.bpi?.GBP?.rate_float
                else -> data.first()?.bpi?.EUR?.rate_float
            }
            return rate ?: 0.0F
        }
        return 0.0F
    }
}