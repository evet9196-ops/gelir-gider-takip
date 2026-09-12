package com.defter.harcama.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.defter.harcama.data.Entry
import java.text.NumberFormat
import java.util.*

fun formatTL(amount: Double): String {
    val nf = NumberFormat.getNumberInstance(Locale("tr", "TR"))
    nf.maximumFractionDigits = 2
    return "₺" + nf.format(amount)
}

@Composable
fun ListScreen(entries: List<Entry>, onDelete: (Entry) -> Unit) {
    if (entries.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Henüz kayıt yok. \"Ekle\" sekmesinden başlayın.", color = TextDim)
        }
        return
    }
    LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        items(entries, key = { it.id }) { entry ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(entry.desc, fontWeight = FontWeight.Bold, color = TextColor)
                    Text(
                        "${entry.date} · ${entry.category} · ${entry.pay}",
                        color = TextDim,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val amtColor = if (entry.type == "income") GoldColor else RustColor
                    val sign = if (entry.type == "income") "+" else "-"
                    Text("$sign${formatTL(entry.amount)}", color = amtColor, fontWeight = FontWeight.Bold)
                    IconButton(onClick = { onDelete(entry) }) {
                        Icon(Icons.Default.Close, contentDescription = "Sil", tint = TextDim)
                    }
                }
            }
            Divider(color = LineColor)
        }
    }
}
