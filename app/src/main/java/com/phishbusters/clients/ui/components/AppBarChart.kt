package com.phishbusters.clients.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val defaultMaxHeight = 200.dp


@Composable
internal fun BarChart(
    modifier: Modifier = Modifier,
    values: List<Float>,
    legends: List<String>,
    maxHeight: Dp = defaultMaxHeight
) {
    assert(values.isNotEmpty()) { "Input values are empty" }
    assert(values.size == legends.size) { "Values and legends must have the same size" }

    val borderColor = MaterialTheme.colorScheme.primary
    val density = LocalDensity.current
    val strokeWidth = with(density) { 1.dp.toPx() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(maxHeight + 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(maxHeight)
                .drawBehind {
                    // draw X-Axis
                    drawLine(
                        color = borderColor,
                        start = Offset(0f, size.height - strokeWidth),
                        end = Offset(size.width, size.height - strokeWidth),
                        strokeWidth = strokeWidth
                    )
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            values.forEach { item ->
                Bar(
                    value = item,
                    color = MaterialTheme.colorScheme.primary,
                    maxHeight = maxHeight,
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            legends.forEach { legend ->
                Text(
                    text = legend,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun RowScope.Bar(
    value: Float,
    color: Color,
    maxHeight: Dp
) {

    val itemHeight = remember(value) { value * maxHeight.value / 100 }
    Spacer(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .height(itemHeight.dp)
            .weight(1f)
            .background(color)
    )
}