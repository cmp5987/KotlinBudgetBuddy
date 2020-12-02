package edu.rit.cmp5987.budgetbuddy.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//provide data to the UI and survive configuration changes
//ViewModel acts as communication center between Repository and the UI
class TransactionViewModel(application: Application): AndroidViewModel(application) {
    val readAllData: LiveData<List<Transaction>>
    private val repository: TransactionRepository

    init{
        val transactionDao = TransactionDatabase.getDatabase(application).transactionDao()
        repository = TransactionRepository(transactionDao)
        readAllData = repository.readAllData
    }

    fun addTransaction(transaction: Transaction){
        viewModelScope.launch(Dispatchers.IO){
            repository.addTransaction(transaction)
        }
    }
}