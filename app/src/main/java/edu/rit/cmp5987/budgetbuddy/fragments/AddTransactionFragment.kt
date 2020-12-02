package edu.rit.cmp5987.budgetbuddy.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import edu.rit.cmp5987.budgetbuddy.DatePickerHelper
import edu.rit.cmp5987.budgetbuddy.R
import edu.rit.cmp5987.budgetbuddy.data.Transaction
import edu.rit.cmp5987.budgetbuddy.data.TransactionViewModel
import kotlinx.android.synthetic.main.fragment_add_transaction.*
import java.util.*

class AddTransactionFragment : Fragment() {
    private var cal = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    lateinit var datePicker: DatePickerHelper
    private  lateinit var mTransactionViewModel: TransactionViewModel

    //set to first value by default
    lateinit var frequencyPicked : String
    lateinit var transactionPicked : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_transaction, container, false)

        //setup viewModel
        mTransactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)

        //setup transaction spinner
        val transactionTypes = resources.getStringArray(R.array.entry_type)
        val transactionSpinner = view.findViewById<Spinner>(R.id.spinner_transaction_type)
        if(transactionSpinner != null){
            //specify layout and list
            val transactionAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, transactionTypes)
            transactionAdapter.setDropDownViewResource((android.R.layout.simple_spinner_dropdown_item))
            //apply adapter
            transactionSpinner.adapter = transactionAdapter

            //listen for changes to value
            transactionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    transactionPicked = transactionTypes[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //Nothing happens
                }

            }
        }

        //setup frequency spinner
        val frequencyTypes = resources.getStringArray(R.array.frequency_types)
        val frequencySpinner = view.findViewById<Spinner>(R.id.spinner_frequency)
        if(frequencySpinner != null){
            //specify layout and list
            val frequencyAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, frequencyTypes)
            frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            //apply adapter
            frequencySpinner.adapter = frequencyAdapter

            frequencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    frequencyPicked = frequencyTypes[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //Nothing happens
                }

            }
        }

        //store late init variable
        frequencyPicked = frequencyTypes[0]
        transactionPicked = transactionTypes[0]

        //setup date picker
        datePicker = DatePickerHelper(requireContext())
        view.findViewById<EditText>(R.id.et_date).setOnClickListener {
            showDatePickerDialog()
        }

        //handle button
        view.findViewById<Button>(R.id.btn_save).setOnClickListener{
            insertDataToDatabase()
        }

        return view
    }
    private fun insertDataToDatabase(){
        val name = et_entry_name.text.toString()
        val amount = et_entry_amount.text.toString()
        val startDate = et_date.text.toString()
        val dateArray = startDate.split("/")

        if(inputCheck(name, amount, startDate)){
            val transaction = Transaction(0, name, amount.toDouble(), frequencyPicked, transactionPicked, Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1]), Integer.parseInt(dateArray[2]))
            mTransactionViewModel.addTransaction(transaction)
            Toast.makeText(requireContext(), "Successfully added", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_addTransactionFragment_to_transactionListFragment)
        }
        else{
            Toast.makeText(requireContext(), "Please fill out all fields before saving!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun inputCheck(name: String, amount: String, date: String): Boolean{
        //ensure fields are filled
        return !(TextUtils.isEmpty(name) && TextUtils.isEmpty(amount) && TextUtils.isEmpty(date))
    }

    private fun showDatePickerDialog() {
        val cal = Calendar.getInstance()
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val m = cal.get(Calendar.MONTH)
        val y = cal.get(Calendar.YEAR)

        val minDate = Calendar.getInstance()
        minDate.set(Calendar.DAY_OF_MONTH, 1)
        datePicker.setMinDate(minDate.timeInMillis)

        val maxDate = Calendar.getInstance()
        maxDate.add(Calendar.MONTH, 1)
        maxDate.set(Calendar.DAY_OF_MONTH, 0)
        datePicker.setMaxDate(maxDate.timeInMillis)

        datePicker.showDialog(d, m, y, object : DatePickerHelper.Callback {

            override fun onDateSelected(dayofMonth: Int, month: Int, year: Int) {
                val dayStr = if (dayofMonth < 10) "0${dayofMonth}" else "${dayofMonth}"

                val mon = month + 1
                val monthStr = if (mon < 10) "0${mon}" else "${mon}"

                et_date.setText("${dayStr}/${monthStr}/${year}")
            }
        })
    }

}