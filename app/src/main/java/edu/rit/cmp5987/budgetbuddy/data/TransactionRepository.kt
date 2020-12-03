package edu.rit.cmp5987.budgetbuddy.data

import androidx.lifecycle.LiveData

class TransactionRepository(private val transactionDao: TransactionDao){
    val readAllData : LiveData<List<Transaction>> = transactionDao.readAllData()

    suspend fun addTransaction(transaction: Transaction){
        transactionDao.addTransaction(transaction)
    }
    suspend fun updateTransaction(transaction: Transaction){
        transactionDao.updateTransaction(transaction)
    }
    suspend fun deleteTransaction(transaction: Transaction){
        transactionDao.deleteTransaction(transaction)
    }
}