package edu.rit.cmp5987.budgetbuddy.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import edu.rit.cmp5987.budgetbuddy.DatePickerHelper
import edu.rit.cmp5987.budgetbuddy.R
import edu.rit.cmp5987.budgetbuddy.data.Transaction
import edu.rit.cmp5987.budgetbuddy.data.TransactionViewModel
import kotlinx.android.synthetic.main.fragment_add_transaction.*
import kotlinx.android.synthetic.main.fragment_update_transaction.*
import kotlinx.android.synthetic.main.fragment_update_transaction.view.*
import java.util.*

class UpdateTransactionFragment : Fragment() {
    private var cal = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    lateinit var datePicker: DatePickerHelper

    private val args by navArgs<UpdateTransactionFragmentArgs>()
    private lateinit var mTransactionViewModel: TransactionViewModel

    //set to first value by default
    lateinit var frequencyPicked : String
    lateinit var transactionPicked : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_transaction, container, false)
        val transactionTypes = resources.getStringArray(R.array.entry_type)

        val frequencyTypes = resources.getStringArray(R.array.frequency_types)

        val transactionSpinner = view.findViewById<Spinner>(R.id.spinner_transaction_type_update)
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

        val frequencySpinner = view.findViewById<Spinner>(R.id.spinner_frequency_update)
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

        //setup date picker
        datePicker = DatePickerHelper(requireContext())
        view.findViewById<EditText>(R.id.et_date_update).setOnClickListener {
            showDatePickerDialog()
        }

        mTransactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)
        view.et_entry_name_update.setText(args.currentTransaction.name)
        view.et_entry_amount_update.setText(args.currentTransaction.amount.toString())
        view.et_date_update.setText(args.currentTransaction.startDay.toString() + "/" + args.currentTransaction.startMonth.toString() + "/" + args.currentTransaction.startYear.toString())
        view.spinner_transaction_type_update.setSelection(transactionTypes.indexOf(args.currentTransaction.type))
        view.spinner_frequency_update.setSelection(frequencyTypes.indexOf(args.currentTransaction.frequency))
        transactionPicked = args.currentTransaction.type
        frequencyPicked = args.currentTransaction.frequency

        view.btn_update.setOnClickListener{
            updateItem()
        }

        setHasOptionsMenu(true)

        return view
    }
    private fun updateItem(){
        val name = et_entry_name_update.text.toString()
        val amount = et_entry_amount_update.text.toString()
        val startDate = et_date_update.text.toString()
        val dateArray = startDate.split("/")

        if(inputCheck(name, amount, startDate)){
            val transaction = Transaction(args.currentTransaction.id, name, amount.toDouble(), frequencyPicked, transactionPicked, Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1]), Integer.parseInt(dateArray[2]))
            mTransactionViewModel.updateTransaction(transaction)
            Toast.makeText(requireContext(), "Successfully updated", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updateTransactionFragment_to_transactionListFragment)
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

                et_date_update.setText("${dayStr}/${monthStr}/${year}")
            }
        })
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater){
        //super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_delete){
            deleteTransaction()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun deleteTransaction(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Entry?")
        builder.setMessage("Are you sure you wish to delete: ${args.currentTransaction.name}")
        builder.setPositiveButton("Delete") { dialog, which ->
            mTransactionViewModel.deleteTransaction(args.currentTransaction)
            Toast.makeText(requireContext(), "Successfully removed: ${args.currentTransaction.name}", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateTransactionFragment_to_transactionListFragment)
        }
        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            Toast.makeText(requireContext(),
                android.R.string.no, Toast.LENGTH_SHORT).show()
        }
        val alertDialog = builder.create()
        alertDialog.show()
        val button = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        with(button) {
            setBackgroundColor(Color.parseColor("#00796b"))
            setTextColor(Color.WHITE)
        }
    }
}