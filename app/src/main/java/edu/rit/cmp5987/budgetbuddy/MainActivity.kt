package edu.rit.cmp5987.budgetbuddy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBarWithNavController(findNavController(R.id.primaryFragment))

        //shift up for the soft keyboard
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.primaryFragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}