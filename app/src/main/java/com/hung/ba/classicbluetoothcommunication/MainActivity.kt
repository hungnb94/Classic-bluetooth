package com.hung.ba.classicbluetoothcommunication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openTransferDataScreen()
    }

    private fun openTransferDataScreen() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content, TransferDataFragment.newInstance())
            .commit()
    }
}