package edu.rit.cmp5987.budgetbuddy.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rit.cmp5987.budgetbuddy.R
import edu.rit.cmp5987.budgetbuddy.TransactionListAdapter
import edu.rit.cmp5987.budgetbuddy.data.TransactionViewModel
import kotlinx.android.synthetic.main.fragment_transaction_list.view.*

class TransactionListFragment : Fragment() {

    private lateinit var mTransactionViewModel: TransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_transaction_list, container, false)

        //Recycler view model
        val adapter = TransactionListAdapter()
        val recyclerView = view.recyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mTransactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)
        mTransactionViewModel.readAllData.observe(viewLifecycleOwner, Observer { transaction ->
            adapter.setData(transaction)
        })


        view.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_transactionListFragment_to_addTransactionFragment)
        }

        return view
    }

}