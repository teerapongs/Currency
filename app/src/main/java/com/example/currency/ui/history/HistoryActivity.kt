package com.example.currency.ui.history

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currency.base.BaseActivity
import com.example.currency.databinding.ActivityHistoryBinding
import com.example.currency.ui.history.adpter.HistoryAdapter

class HistoryActivity : BaseActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private val historyAdapter: HistoryAdapter by lazy { HistoryAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupRecyclerView() {
        binding.historyRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = historyAdapter
        }

        val data = userData.getHistoryCurrencyMap()
        historyAdapter.apply {
            setItems(data)
        }

        historyAdapter.notifyDataSetChanged()
    }
}