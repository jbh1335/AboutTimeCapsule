package com.aboutcapsule.android.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.SpinnerCapsuleboardItemBinding

class SpinnerGroupAvailAdapter (
    context: Context,
    @LayoutRes private val resId:Int,
    private val values: MutableList<String>
        ) : ArrayAdapter<String>(context, resId, values) {

    override fun getCount() = values.size

    override fun getItem(position: Int) = values[position]

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = SpinnerCapsuleboardItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val model = values[position]

        try{
            binding.text.text=model
            binding.text.setBackgroundResource(R.drawable.btn_radius_rectangle)

            binding.text.gravity=Gravity.CENTER
            binding.text.setTextColor(ContextCompat.getColor(context,R.color.white))
            binding.text.textSize = 7f
            val typeface = Typeface.createFromAsset(context.assets, "font/onemobiletitle.ttf")
            binding.text.typeface= typeface

        }catch (e: Exception){
            e.printStackTrace()
        }
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = SpinnerCapsuleboardItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val model = values[position]

        try{
            binding.text.text=model
            binding.text.setBackgroundResource(R.color.skyblueBtnColor)
            binding.text.gravity=Gravity.CENTER
            binding.text.setTextColor(ContextCompat.getColor(context,R.color.btnColor))
            binding.text.textSize = 7f
            val typeface = Typeface.createFromAsset(context.assets, "font/onemobiletitle.ttf")
            binding.text.typeface= typeface

        }catch (e: Exception){
            e.printStackTrace()
        }
        return binding.root
    }
}