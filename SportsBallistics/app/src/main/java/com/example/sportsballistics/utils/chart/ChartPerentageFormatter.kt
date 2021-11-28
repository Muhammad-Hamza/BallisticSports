package com.example.sportsballistics.utils.chart

import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import java.text.DecimalFormat


class ChartPerentageFormatter : ValueFormatter() {
    private val mFormat: DecimalFormat
    override fun getFormattedValue(value: Float): String {
        return "" + value.toInt() + "" + " %" // e.g. append a dollar-sign
    }

    fun getFormattedValue(
        value: Float,
        entry: Map.Entry<*, *>?,
        dataSetIndex: Int,
        viewPortHandler: ViewPortHandler?
    ): String {
        // write your logic here
        return "" + mFormat.format(value).toInt() + "" + " %" // e.g. append a dollar-sign
    }

    init {
        mFormat = DecimalFormat("###,###,##0.0") // use one decimal
    }
}