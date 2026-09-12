package com.defter.harcama.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.defter.harcama.data.Categories
import com.defter.harcama.data.Entry
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(onSave: (Entry) -> Unit) {
    var type by remember { mutableStateOf("expense") }
    var amountText by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(Categories.EXPENSE.first()) }
    var pay by remember { mutableStateOf("Kart") }
    val today = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) }
    var date by remember { mutableStateOf(today) }
    var error by remember { mutableStateOf<String?>(null) }

    val cats = if (type == "expense") Categories.EXPENSE else Categories.INCOME
    LaunchedEffect(type) { category = cats.first() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Gider / Gelir switch
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            SegButton("Gider", type == "expense", RustColor, Modifier.weight(1f)) { type = "expense" }
            SegButton("Gelir", type == "income", GoldColor, Modifier.weight(1f)) { type = "income" }
        }
        Spacer(Modifier.height(14.dp))

        OutlinedTextField(
            value = amountText,
            onValueChange = { amountText = it },
            label = { Text("Tutar (₺)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Tarih (yyyy-aa-gg)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = desc,
            onValueChange = { desc = it },
            label = { Text("Açıklama (örn. su, market, maaş...)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(10.dp))

        Text("Kategori", style = MaterialTheme.typography.labelMedium, color = TextDim)
        Spacer(Modifier.height(6.dp))
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
            OutlinedTextField(
                value = category,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                cats.forEach { c ->
                    DropdownMenuItem(text = { Text(c) }, onClick = { category = c; expanded = false })
                }
            }
        }
        Spacer(Modifier.height(10.dp))

        Text("Ödeme yöntemi", style = MaterialTheme.typography.labelMedium, color = TextDim)
        Spacer(Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SegButton("Kart", pay == "Kart", TextColor, Modifier.weight(1f)) { pay = "Kart" }
            SegButton("Nakit", pay == "Nakit", TextColor, Modifier.weight(1f)) { pay = "Nakit" }
        }
        Spacer(Modifier.height(16.dp))

        error?.let {
            Text(it, color = RustColor, modifier = Modifier.padding(bottom = 8.dp))
        }

        Button(
            onClick = {
                val amount = amountText.replace(",", ".").toDoubleOrNull()
                if (amount == null || amount <= 0.0) {
                    error = "Geçerli bir tutar girin"
                    return@Button
                }
                error = null
                onSave(
                    Entry(
                        type = type,
                        amount = amount,
                        date = date,
                        desc = desc.ifBlank { if (type == "expense") "Gider" else "Gelir" },
                        category = category,
                        pay = pay
                    )
                )
                amountText = ""
                desc = ""
            },
            colors = ButtonDefaults.buttonColors(containerColor = GoldColor, contentColor = Color(0xFF1B2E22)),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Kaydet", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SegButton(label: String, selected: Boolean, accent: androidx.compose.ui.graphics.Color, modifier: Modifier, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(46.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) accent.copy(alpha = 0.18f) else SurfaceColor2,
            contentColor = if (selected) accent else TextDim
        )
    ) {
        Text(label, fontWeight = FontWeight.Bold)
    }
}
