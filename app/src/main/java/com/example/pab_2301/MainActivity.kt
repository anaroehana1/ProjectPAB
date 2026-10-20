package com.example.pab_2301

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val edtNama = findViewById<EditText>(R.id.edtNama)
        val rgJk = findViewById<RadioGroup>(R.id.rgJk)
        val spKota = findViewById<Spinner>(R.id.spKota)
        val swMenyetujui = findViewById<Switch>(R.id.swMenyetujui)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        val kota = arrayOf("Pilih Kota", "Jakarta", "Bandung", "Surabaya", "Padang")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, kota)
        spKota.adapter = adapter

        btnSubmit.setOnClickListener {
            if (edtNama.text.toString().isEmpty()) {
                Toast.makeText(this, "Nama Tidak Boleh Kosong!!!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!swMenyetujui.isChecked) {
                Toast.makeText(this, "Anda Harus Menyetujui Syarat dan Ketentuan!!!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val jk = when (rgJk.checkedRadioButtonId) {
                R.id.rbLaki -> "Laki-Laki"
                R.id.rbPerempuan -> "Perempuan"
                else -> "Belum Dipilih"
            }

            val data = "Nama: ${edtNama.text}" +
                    "\nJenis Kelamin: $jk" +
                    "\nKota: ${spKota.selectedItem}"
            val intent = Intent(this, DuaActivity::class.java)
            intent.putExtra("DATA_HASIL", data)
            startActivity(intent)
        }
    }
}
