package edu.rit.cmp5987.budgetbuddy.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Transaction::class], version = 1, exportSchema = false)
abstract class TransactionDatabase: RoomDatabase() {
    abstract fun transactionDao(): TransactionDao

    companion object{
        //instance variable
        @Volatile
        private var INSTANCE: TransactionDatabase? = null

        //get Database check, if instance already exists and return it or create a new one
        fun getDatabase(context: Context): TransactionDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        TransactionDatabase::class.java,
                        "transaction_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}