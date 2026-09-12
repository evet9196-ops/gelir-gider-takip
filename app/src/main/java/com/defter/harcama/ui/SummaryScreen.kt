package com.defter.harcama.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.defter.harcama.data.Categories
import com.defter.harcama.data.Entry
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SummaryScreen(entries: List<Entry>) {
    var monthOffset by remember { mutableStateOf(0) }
    val cal = remember(monthOffset) {
        Calendar.getInstance().apply { add(Calendar.MONTH, monthOffset) }
    }
    val keyFmt = SimpleDateFormat("yyyy-MM", Locale.getDefault())
    val labelFmt = SimpleDateFormat("MMMM yyyy", Locale("tr", "TR"))
    val monthKey = keyFmt.format(cal.time)
    val monthEntries = entries.filter { it.date.startsWith(monthKey) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { monthOffset-- }) { Text("‹", color = TextColor) }
            Text(labelFmt.format(cal.time).replaceFirstChar { it.uppercase() }, fontWeight = FontWeight.Bold)
            IconButton(onClick = { monthOffset++ }) { Text("›", color = TextColor) }
        }
        Spacer(Modifier.height(12.dp))

        val income = monthEntries.filter { it.type == "income" }.sumOf { it.amount }
        val expense = monthEntries.filter { it.type == "expense" }.sumOf { it.amount }
        val net = income - expense

        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(18.dp)) {
                Text("Net durum", color = TextDim, style = MaterialTheme.typography.bodySmall)
                Text(
                    formatTL(net),
                    color = if (net < 0) RustColor else TextColor,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    Column {
                        Text("Gelir", color = TextDim, style = MaterialTheme.typography.bodySmall)
                        Text(formatTL(income), color = GoldColor, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text("Gider", color = TextDim, style = MaterialTheme.typography.bodySmall)
                        Text(formatTL(expense), color = RustColor, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        CategoryChart("Gider — kategori dağılımı", monthEntries.filter { it.type == "expense" }, Categories.EXPENSE, RustColor)
        Spacer(Modifier.height(16.dp))
        CategoryChart("Gelir — kategori dağılımı", monthEntries.filter { it.type == "income" }, Categories.INCOME, GoldColor)
    }
}

@Composable
private fun CategoryChart(title: String, list: List<Entry>, cats: List<String>, color: androidx.compose.ui.graphics.Color) {
    Card(colors = CardDefaults.cardColors(containerColor = SurfaceColor), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(title, color = TextDim, style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(10.dp))
            if (list.isEmpty()) {
                Text("Bu ay kayıt yok", color = TextDim, style = MaterialTheme.typography.bodySmall)
                return@Column
            }
            val totals = cats.associateWith { c -> list.filter { it.category == c }.sumOf { it.amount } }
            val max = (totals.values.maxOrNull() ?: 1.0).coerceAtLeast(1.0)
            totals.filter { it.value > 0 }.forEach { (cat, total) ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(cat, style = MaterialTheme.typography.bodySmall)
                    Text(formatTL(total), style = MaterialTheme.typography.bodySmall)
                }
                Spacer(Modifier.height(4.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(SurfaceColor2)
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth((total / max).toFloat())
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(color)
                    )
                }
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}
