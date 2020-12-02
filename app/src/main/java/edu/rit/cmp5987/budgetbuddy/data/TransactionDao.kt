package edu.rit.cmp5987.budgetbuddy.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTransaction(transaction: Transaction)

    @Query("SELECT * FROM transaction_table ORDER BY startDay DESC")
    fun readAllData(): LiveData<List<Transaction>>

}