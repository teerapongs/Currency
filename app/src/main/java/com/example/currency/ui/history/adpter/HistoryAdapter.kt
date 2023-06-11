package com.example.currency.ui.history.adpter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.currency.base.BaseViewHolder
import com.example.currency.databinding.ContentListHistoryPriceCurrencyBinding
import com.example.currency.model.Currency

class HistoryAdapter: RecyclerView.Adapter<BaseViewHolder>() {

    private var itemsMap: Map<String?, Currency?>? = null

    fun setItems(items: Map<String?, Currency?>?) {
        this.itemsMap = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return HistoryViewHolder(
            ContentListHistoryPriceCurrencyBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = itemsMap?.size ?: 0

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when(holder) {
            is HistoryViewHolder -> {
                val item = itemsMap?.toList()
                holder.onBindData(item?.get(position)?.second)
            }
        }
    }
}