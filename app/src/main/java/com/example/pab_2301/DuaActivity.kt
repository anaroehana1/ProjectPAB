package com.example.pab_2301

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DuaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dua)

        val pbLoading = findViewById<ProgressBar>(R.id.pbLoading)
        val tvHasil = findViewById<TextView>(R.id.tvHasil)
        val btnKembali = findViewById<Button>(R.id.btnKembali)
        val btnLanjut = findViewById<Button>(R.id.btnLanjut)

        val data = intent.getStringExtra("DATA_HASIL")
        tvHasil.text = data

        Thread {
            var progress = 0
            val handler = Handler(Looper.getMainLooper())
            while (progress < 100) {
                try {
                    Thread.sleep(30)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                progress++
                val p = progress
                handler.post { pbLoading.progress = p }
            }
        }.start()

        btnLanjut.setOnClickListener {
            val intent = Intent(this, TigaActivity::class.java)
            intent.putExtra("EXTRA_INFO", "Informasi Lanjutan untuk: $data")
            startActivity(intent)
        }

        btnKembali.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }
}
