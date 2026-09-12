package com.defter.harcama

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.defter.harcama.ui.*

class MainActivity : ComponentActivity() {

    private val viewModel: EntryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DefterTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppRoot(viewModel)
                }
            }
        }
    }
}

@Composable
fun AppRoot(viewModel: EntryViewModel) {
    val entries by viewModel.entries.collectAsState()
    var tab by remember { mutableStateOf(0) }
    val tabs = listOf("Ekle", "Kayıtlar", "Ay Özeti")

    Column(Modifier.fillMaxSize()) {
        Text(
            "Defter",
            modifier = Modifier.padding(20.dp, 16.dp, 20.dp, 8.dp),
            style = MaterialTheme.typography.headlineSmall
        )
        TabRow(selectedTabIndex = tab, containerColor = SurfaceColor) {
            tabs.forEachIndexed { i, label ->
                Tab(selected = tab == i, onClick = { tab = i }, text = { Text(label) })
            }
        }
        Box(Modifier.weight(1f)) {
            when (tab) {
                0 -> AddScreen(onSave = { viewModel.addEntry(it) })
                1 -> ListScreen(entries = entries, onDelete = { viewModel.deleteEntry(it) })
                2 -> SummaryScreen(entries = entries)
            }
        }
    }
}
