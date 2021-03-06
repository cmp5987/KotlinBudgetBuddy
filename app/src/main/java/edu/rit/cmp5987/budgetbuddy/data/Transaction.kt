package edu.rit.cmp5987.budgetbuddy.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "transaction_table")
class Transaction (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val amount: Double,
    val frequency: String,
    val type: String,
    val startDay: Int,
    val startMonth: Int,
    val startYear: Int
): Parcelable