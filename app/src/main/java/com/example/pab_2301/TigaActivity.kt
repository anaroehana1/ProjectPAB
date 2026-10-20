package com.example.pab_2301

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TigaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tiga)

        val btnSelesai = findViewById<Button>(R.id.btnSelesai)
        val tvInfo = findViewById<TextView>(R.id.tvInfo)

        val receivedData = intent.getStringExtra("EXTRA_INFO")
        if (!receivedData.isNullOrEmpty()) {
            tvInfo.text = receivedData
        }

        btnSelesai.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}
