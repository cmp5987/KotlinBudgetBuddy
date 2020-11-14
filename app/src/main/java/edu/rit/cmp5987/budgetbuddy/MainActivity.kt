package edu.rit.cmp5987.budgetbuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fabAddEntry.setOnClickListener {
            val intent = Intent(this@MainActivity, EditEntryActivity::class.java)
            startActivity(intent)
        }
    }
}