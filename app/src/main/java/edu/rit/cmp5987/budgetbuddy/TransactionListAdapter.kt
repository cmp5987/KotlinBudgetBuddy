package edu.rit.cmp5987.budgetbuddy

import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import edu.rit.cmp5987.budgetbuddy.data.Transaction
import edu.rit.cmp5987.budgetbuddy.fragments.TransactionListFragmentDirections
import kotlinx.android.synthetic.main.transaction_row.view.*
import java.util.*

class TransactionListAdapter: RecyclerView.Adapter<TransactionListAdapter.MyViewHolder>() {

    private var transactionList = emptyList<Transaction>()
    private var monthList = arrayOf("January", "February",
            "March", "April", "May", "June", "July",
            "August", "September", "October", "November",
            "December")

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionListAdapter.MyViewHolder {
       return  MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.transaction_row, parent, false))
    }

    override fun onBindViewHolder(holder: TransactionListAdapter.MyViewHolder, position: Int) {
        val currentItem = transactionList[position]
        val cal = Calendar.getInstance()
        //we match with the current month rather than using the item because if it is passed here it might be a monthly repeated item with a different startMonth
        val currentMonth = monthList[cal.get(Calendar.MONTH)]

        when(currentItem.type){
            "Expense: Need" -> {
                //handle as an expense
                holder.itemView.transaction_icon_iv.setImageResource(R.drawable.ic_baseline_food_bank_48)
                holder.itemView.transaction_amount_tv.setTextColor(Color.parseColor("#bf360c"))
                holder.itemView.transaction_amount_tv.text = "-" + currentItem.amount.toString()
            }
            "Expense: Want" -> {
                //handle as an expense
                holder.itemView.transaction_icon_iv.setImageResource(R.drawable.ic_baseline_emoji_emotions_48)
                holder.itemView.transaction_amount_tv.setTextColor(Color.parseColor("#bf360c"))
                holder.itemView.transaction_amount_tv.text = "-" + currentItem.amount.toString()
            }
            "New Income" -> {
                //handle as an income
                holder.itemView.transaction_icon_iv.setImageResource(R.drawable.ic_baseline_monetization_on_48)
                holder.itemView.transaction_amount_tv.setTextColor(Color.parseColor("#1b5e20"))
                holder.itemView.transaction_amount_tv.text = "+" + currentItem.amount.toString()
            }
        }

        holder.itemView.transaction_entry_tv.text = currentItem.name
        holder.itemView.transaction_date_tv.text =  currentMonth + " " + currentItem.startDay.toString()
        holder.itemView.transaction_type_tv.text = currentItem.type


        ///handle click to update
        holder.itemView.rowTransactionLayout.setOnClickListener{
            //pass tranaction object to update the fragment
            val action = TransactionListFragmentDirections.actionTransactionListFragmentToUpdateTransactionFragment(currentItem)
            //when item is selected, pass the current Item from the list fragment to the item fragment
            holder.itemView.findNavController().navigate(action)
        }

    }

    override fun getItemCount(): Int {
        return transactionList.size
    }
    fun setData(transaction: List<Transaction>){
        this.transactionList = transaction
        notifyDataSetChanged()
    }
    private fun inputCheck(name: String, amount: String, date: String): Boolean{
        //ensure fields are filled
        return !(TextUtils.isEmpty(name) && TextUtils.isEmpty(amount) && TextUtils.isEmpty(date))
    }
}