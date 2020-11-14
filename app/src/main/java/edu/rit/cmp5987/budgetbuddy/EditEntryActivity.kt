package edu.rit.cmp5987.budgetbuddy

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

class EditEntryActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var list_of_items = arrayOf("Select Entry Type","Needs Type Expense", "Wants Type Expense", "Source of Income")
    private lateinit var spinner : MaterialSpinner
    private var selectedSpinnerValue : String? = null

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

        spinner = spinner_entry_type
        spinner.getSpinner().onItemSelectedListener = this

        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_of_items)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        spinner!!.setAdapter(aa)
    }
    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
        // use position to know the selected item
        if(position > 0)
        {
            spinner.setErrorEnabled(false)
            spinner.setLabel("Entry Type")
            Toast.makeText(this, list_of_items[position], Toast.LENGTH_LONG).show()
            selectedSpinnerValue = list_of_items[position]
        }
        else{
            selectedSpinnerValue = null
        }
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            else -> super.onOptionsItemSelected(item)
        }
    }
}