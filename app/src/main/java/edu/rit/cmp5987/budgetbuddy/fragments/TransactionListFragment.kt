package edu.rit.cmp5987.budgetbuddy.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.findFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.rit.cmp5987.budgetbuddy.R
import edu.rit.cmp5987.budgetbuddy.TransactionListAdapter
import edu.rit.cmp5987.budgetbuddy.data.Transaction
import edu.rit.cmp5987.budgetbuddy.data.TransactionViewModel
import kotlinx.android.synthetic.main.fragment_transaction_list.*
import kotlinx.android.synthetic.main.fragment_transaction_list.view.*
import java.lang.Integer.parseInt
import java.util.*

class TransactionListFragment : Fragment() {

    private lateinit var mTransactionViewModel: TransactionViewModel
    private lateinit var updatedList: List<Transaction>
    private var monthList = arrayOf("January", "February",
        "March", "April", "May", "June", "July",
        "August", "September", "October", "November",
        "December")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_transaction_list, container, false)

        //current calendar
        val cal = Calendar.getInstance()
        val month = cal.get(Calendar.MONTH) + 1
        val year =  cal.get(Calendar.YEAR)

        //Recycler view model
        val adapter = TransactionListAdapter()
        val recyclerView = view.recyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mTransactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)
        mTransactionViewModel.readAllData.observe(viewLifecycleOwner, Observer { transaction ->
            run {
                updatedList = transaction.filter {
                    matchDateYear(
                        it,
                        month,
                        year
                    ) || matchMonthRepeatYearly(it, month) || matchMonthly(it)
                }
                adapter.setData(updatedList)
                updatedSummary(updatedList, view, month, year)
            }
        })

        view.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_transactionListFragment_to_addTransactionFragment)
        }
        /*if(childFragmentManager.findFragmentById(R.id.summary) == null){
            monthlySummaryFragment = MonthlySummaryFragment()
            childFragmentManager.beginTransaction().add(R.id.summary, monthlySummaryFragment).commit()
        }*/

        return view
    }
    private fun updatedSummary(transactionList: List<Transaction>, view: View, month: Int, year: Int){
        var needAmount = 0.0
        var needBudget = 0.0
        var wantAmount = 0.0
        var wantBudget = 0.0
        var savingsAmount = 0.0
        var incomeAmount = 0.0
        val monthTitle = monthList[month -1] + ", $year"

        view.findViewById<TextView>(R.id.monthTitle).text = monthTitle

        for(item in transactionList){
            if(item.type == "Expense: Need"){
                needAmount += item.amount
            }
            else if(item.type == "Expense: Want"){
                wantAmount += item.amount
            }
            else if(item.type == "New Income"){
                incomeAmount += item.amount
            }
        }

        if(incomeAmount > 0){
            //Suggested Budgeting rule for needs is 50% of income
            needBudget = incomeAmount * .5
            //Suggested Budgeting rule for wants is 30% of income
            wantBudget = incomeAmount * .3
        }
        //find out money left from current income
        savingsAmount = incomeAmount - (needAmount + wantAmount)

        if(savingsAmount > 0){
            view.findViewById<TextView>(R.id.savings_value_tv).text = "$$savingsAmount"
        }
        else{
            view.findViewById<TextView>(R.id.savings_value_tv).text = "-$$savingsAmount"
        }

        view.findViewById<TextView>(R.id.current_wants_tv).text = "$$wantAmount"
        view.findViewById<TextView>(R.id.current_needs_tv).text = "$$needAmount"
        view.findViewById<TextView>(R.id.limit_needs_budget_tv).text = "$$needBudget"
        view.findViewById<TextView>(R.id.limit_wants_budget_tv).text = "$$wantBudget"

        view.findViewById<ProgressBar>(R.id.needsProgressBar).max = needBudget.toInt()
        view.findViewById<ProgressBar>(R.id.needsProgressBar).progress = needAmount.toInt()

        view.findViewById<ProgressBar>(R.id.wantsProgressBar).max = wantBudget.toInt()
        view.findViewById<ProgressBar>(R.id.wantsProgressBar).progress = wantAmount.toInt()

    }
    /*
        Filters data to only get transactions to only find ones that apply to this month
     */
    private fun matchDateYear(transaction: Transaction, month: Int, year: Int): Boolean{
        //current month and year is correct
        Log.v("Testing", "here " + month.toString() + " " + year.toString() + " " + transaction.startMonth)
        return (transaction.startMonth == month && (transaction.startYear == year))
    }
    private fun matchMonthRepeatYearly(transaction: Transaction, month: Int):Boolean{
        //Handle yearly type, the year of the original entry does not need to be the same as the current year
        return (transaction.startMonth == month && (transaction.type == "Yearly"))
    }
    private fun matchMonthly(transaction: Transaction):Boolean{
        //if type is monthly then allow it to repeat
        return transaction.type == "Monthly"
    }
}