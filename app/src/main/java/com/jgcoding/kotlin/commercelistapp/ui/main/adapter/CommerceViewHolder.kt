package com.jgcoding.kotlin.commercelistapp.ui.main.adapter

import android.content.Context
import android.graphics.Color
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.jgcoding.kotlin.commercelistapp.R
import com.jgcoding.kotlin.commercelistapp.core.Converter
import com.jgcoding.kotlin.commercelistapp.databinding.ItemCommerceBinding
import com.jgcoding.kotlin.commercelistapp.domain.model.Commerce

class CommerceViewHolder(
    private val context: Context,
    private val binding: ItemCommerceBinding,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        private const val TAG = "CommerceViewHolder"
        private const val FOOD = "FOOD"
        private const val GAS_STATION = "GAS_STATION"
        private const val SHOPPING = "SHOPPING"
        private const val ELECTRIC_STATION = "ELECTRIC_STATION"
        private const val DIRECT_SALES = "DIRECT_SALES"
        private const val LEISURE = "LEISURE"
        private const val BEAUTY = "BEAUTY"
    }


    fun bind(commerce: Commerce) {
        binding.apply {

            Glide.with(context)
                .load(commerce.photo)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .transform( CenterCrop(), RoundedCorners(16))
                .into(ivCommerceImage)

            tvCommerceName.text = commerce.name ?: ""
            tvCommerceDescription.text = commerce.address ?: ""
            val distance = if(commerce.distance > 1000) "${Converter.convertIntoKms(commerce.distance.toDouble())}km" else "${commerce.distance}m."
            tvDistance.text = distance

            Log.i(TAG, "Category: ${commerce.category}")
            when (commerce.category) {
                FOOD -> {
                    ivCategory.setImageResource(R.drawable.catering_white)
                    clCategory.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow))
                }
                GAS_STATION -> {
                    ivCategory.setImageResource(R.drawable.ees_white)
                    clCategory.setBackgroundColor(ContextCompat.getColor(context, R.color.orange))
                }
                SHOPPING -> {
                    ivCategory.setImageResource(R.drawable.cart_white)
                    clCategory.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_blue))
                }
                ELECTRIC_STATION -> {
                    ivCategory.setImageResource(R.drawable.electric_scooter_white)
                    clCategory.setBackgroundColor(ContextCompat.getColor(context, R.color.orange))
                }
                DIRECT_SALES -> {
                    ivCategory.setImageResource(R.drawable.truck_white)
                    clCategory.setBackgroundColor(ContextCompat.getColor(context, R.color.purple))
                }
                LEISURE -> {
                    ivCategory.setImageResource(R.drawable.leisure_white)
                    clCategory.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_blue))
                }
                BEAUTY -> {
                    ivCategory.setImageResource(R.drawable.car_wash_white)
                    clCategory.setBackgroundColor(ContextCompat.getColor(context, R.color.gray_text_color))
                }
            }

            cvCommerce.setOnClickListener {
                onItemClick(commerce.id)
            }
        }
    }

}