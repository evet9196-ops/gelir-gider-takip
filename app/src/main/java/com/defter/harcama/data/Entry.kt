package com.defter.harcama.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entries")
data class Entry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String,      // "income" | "expense"
    val amount: Double,
    val date: String,      // ISO yyyy-MM-dd
    val desc: String,
    val category: String,
    val pay: String        // "Kart" | "Nakit"
)

object Categories {
    val EXPENSE = listOf("Market", "Ulaşım", "Fatura", "Eğlence", "Sağlık", "Kira", "Diğer")
    val INCOME = listOf("Maaş", "Ek Gelir", "Diğer")
}
