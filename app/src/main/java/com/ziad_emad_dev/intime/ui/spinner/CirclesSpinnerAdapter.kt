package com.ziad_emad_dev.intime.ui.spinner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.ziad_emad_dev.intime.R

class CirclesSpinnerAdapter(context: Context, coloredCircles: ArrayList<SpinnerColoredCircle>) :
    ArrayAdapter<SpinnerColoredCircle>(context, 0, coloredCircles) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {

        var myConvertView = convertView

        if (myConvertView == null) {
            myConvertView = LayoutInflater.from(context)
                .inflate(R.layout.layout_colored_circles_spinner, parent, false)
        }

        val circle = myConvertView!!.findViewById<ImageView>(R.id.circle)
        val currentItem = getItem(position)
        if (currentItem != null) {
            circle.setImageResource(currentItem.coloredCircle)
        }

        return myConvertView
    }

}