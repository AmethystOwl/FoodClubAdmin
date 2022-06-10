package com.example.foodyadminpanel

import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

class BindingAdapter {
    companion object {

        @JvmStatic
        @BindingAdapter("bindImage")
        fun bindImage(imageView: ImageView, imageUrl: String?) {
            imageUrl?.let {
                Glide.with(imageView.context).load(imageUrl).into(imageView)
            }
        }


        @JvmStatic
        @BindingAdapter("bindSpinner")
        fun bindSpinner(spinner: Spinner, state: String?) {
            state?.let {
                val ar = ArrayAdapter.createFromResource(spinner.context!!, R.array.orderState, android.R.layout.simple_spinner_item)
                ar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = ar
                when (state) {
                    "Order placed" -> {
                        spinner.setSelection(0)
                    }
                    "Order accepted" -> {
                        spinner.setSelection(1)

                    }
                    "Order rejected" -> {
                        spinner.setSelection(2)

                    }
                    "Order canceled" -> {
                        spinner.setSelection(3)

                    }
                    "Order's out for delivery" -> {
                        spinner.setSelection(4)

                    }
                    "Order delivered" -> {
                        spinner.setSelection(5)
                    }
                }
            }
        }
    }
}
