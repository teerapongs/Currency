package com.example.currency.ui.history.adpter

import com.example.currency.base.BaseViewHolder
import com.example.currency.databinding.ContentListHistoryPriceCurrencyBinding
import com.example.currency.databinding.LayoutDetailCurrencyBinding
import com.example.currency.model.Currency

class HistoryViewHolder(
    private val binding: ContentListHistoryPriceCurrencyBinding
): BaseViewHolder(binding.root)  {

    private lateinit var detailBinding: LayoutDetailCurrencyBinding

    fun onBindData(data: Currency?) {
        detailBinding = binding.layoutDetailCurrency
        binding.txtTime.text = data?.time?.updated
        detailBinding.txtUsd.text = data?.bpi?.USD?.rate ?: "0.0"
        detailBinding.txtGbp.text = data?.bpi?.GBP?.rate ?: "0.0"
        detailBinding.txtEur.text = data?.bpi?.EUR?.rate ?: "0.0"
    }
}