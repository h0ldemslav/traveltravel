package cz.mendelu.pef.traveltravel.ui.screens.statistics

import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat

class PieChartValueFormatter : ValueFormatter() {

    private val format = DecimalFormat("###,###,##0.0")

    override fun getFormattedValue(value: Float): String {
        return format.format(value) + " %"
    }
}