package com.yung_coder.oluwole.alc_assessement

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_conversion.*

class Conversion : AppCompatActivity() {

    companion object {
        var crypto_value = ""
        var currency_value = ""
        var conversion_rate = 0.0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversion)

        val extra = intent.getBundleExtra("extra")
        crypto_value = extra.getString("crypto_value")
        currency_value = extra.getString("currency_value")
        conversion_rate = extra.getDouble("rate")

        button_first.text = "Convert to ${currency_value.toUpperCase()}"
        button_second.text = "Convert to ${crypto_value.toUpperCase()}"

        button_first.setOnClickListener {
            if(!TextUtils.isEmpty(text_value_convert.text)){
                val result = currencyConvert(text_value_convert.text.toString().toDouble())
                text_result.text = String.format("Conversion Result : \n$currency_value $result")
            }
            else{
                Toast.makeText(this@Conversion, "Enter a Value to convert", Toast.LENGTH_SHORT).show()
            }
        }

        button_second.setOnClickListener{
            if(!TextUtils.isEmpty(text_value_convert.text)){
                text_result.text = "Conversion Result : \n $crypto_value ${cryptoConvert(text_value_convert.text.toString().toDouble())}"
            }
            else{
                Toast.makeText(this@Conversion, "Enter a Value to convert", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun currencyConvert(value: Double): String{
        return (value * conversion_rate).toString()
    }

    fun cryptoConvert(value: Double): String{
        return (value / conversion_rate).toString()
    }
}
