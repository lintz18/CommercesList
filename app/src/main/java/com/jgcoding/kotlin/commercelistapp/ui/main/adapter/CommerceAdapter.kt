package com.jgcoding.kotlin.commercelistapp.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jgcoding.kotlin.commercelistapp.databinding.ItemCommerceBinding
import com.jgcoding.kotlin.commercelistapp.domain.model.Commerce

class CommerceAdapter(
    private var commerceList: MutableList<Commerce>,
    private val onItemClick: (Int) -> Unit,
) : RecyclerView.Adapter<CommerceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommerceViewHolder {
        val binding =
            ItemCommerceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommerceViewHolder(
            context = parent.context,
            binding = binding,
            onItemClick = { id ->
                onItemClick(id)
            }
        )
    }

    override fun getItemCount(): Int {
        return commerceList.size
    }

    override fun onBindViewHolder(holder: CommerceViewHolder, position: Int) {
        holder.bind(commerceList[position])
    }

    fun updateList(it: List<Commerce>) {
        commerceList.clear()
        commerceList.addAll(it)
//        notifyItemInserted(commerceList.size - 1)
        notifyDataSetChanged()
    }
}