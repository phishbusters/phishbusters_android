package com.phishbusters.clients.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.axis.formatter.DecimalFormatAxisValueFormatter
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf


private fun yAxisCustomFormatter(): AxisValueFormatter<AxisPosition.Vertical.Start> {
    return AxisValueFormatter { y, _ ->
        y.toInt().toString()
    }
}

@Composable
fun AppLineChart(
    modifier: Modifier = Modifier,
    data: Map<Float, Float> = mapOf(),
    xValueFormatter: AxisValueFormatter<AxisPosition.Horizontal.Bottom> = DecimalFormatAxisValueFormatter(),
    yValueFormatter: AxisValueFormatter<AxisPosition.Vertical.Start> = yAxisCustomFormatter()
) {
    val chartEntryModel = entryModelOf(data.map { (x, y) ->
        FloatEntry(x, y)
    })

    ProvideChartStyle(
        chartStyle = m3ChartStyle(
            axisLabelColor = Color.Black,
            axisLineColor = Color.Black,
            axisGuidelineColor = Color.DarkGray,
            elevationOverlayColor = Color.White,
            entityColors = listOf(
                MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary
            ),
        ),
    ) {
        Chart(
            chart = lineChart(),
            model = chartEntryModel,
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis(valueFormatter = xValueFormatter),
            modifier = modifier,
            isZoomEnabled = false,
        )
    }
}