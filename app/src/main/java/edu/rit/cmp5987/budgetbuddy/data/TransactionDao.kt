package edu.rit.cmp5987.budgetbuddy.data

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTransaction(transaction: Transaction)

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("SELECT * FROM transaction_table ORDER BY startDay DESC")
    fun readAllData(): LiveData<List<Transaction>>

}