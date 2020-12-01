package edu.rit.cmp5987.budgetbuddy

import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.dev.materialspinner.MaterialSpinner
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_edit_entry.*
import java.text.SimpleDateFormat
import java.util.*

class EditEntryActivity : AppCompatActivity(){

    private var cal = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    lateinit var datePicker: DatePickerHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_entry)

        setSupportActionBar(toolbar_add_entry)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)//use backbutton

        //shift up for the soft keyboard
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        toolbar_add_entry.setNavigationOnClickListener {
            onBackPressed()
        }

        //Spinner Setup
        val spinnerTypeAdapter = ArrayAdapter.createFromResource(this,
            R.array.entry_type, android.R.layout.simple_spinner_item)
        // Specify the layout to use when the list of choices appears
        spinnerTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        spinner_entry_type.adapter = spinnerTypeAdapter

        val spinnerFrequencyAdapter = ArrayAdapter.createFromResource( this, R.array.frequency_types, android.R.layout.simple_spinner_item)
        spinnerFrequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_frequency.adapter = spinnerFrequencyAdapter


        datePicker = DatePickerHelper(this)
        et_date.setOnClickListener {
            showDatePickerDialog()
        }
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