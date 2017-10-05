package com.yung_coder.oluwole.alc_assessement

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.yung_coder.oluwole.alc_assessement.adapters.ExchangeAdapter
import com.yung_coder.oluwole.alc_assessement.model.model
import kotlinx.android.synthetic.main.activity_card.*
import java.util.*

class Card : AppCompatActivity() {

    companion object {
        val currency_array = arrayOf("NGN", "GBP", "CNY", "AUD", "JPY", "USD", "BWP", "BRL", "CAD", "COP", "ESP", "KRW", "INR", "JMD", "KWD", "NZD", "PKR", "QAR", "UAH","EUR")
         val country_array = arrayOf("NGA", "GBR", "CHN", "AUS", "JPN", "USA", "BWA", "BRA", "CAN", "COL", "ESP", "KOR", "IND", "JAM", "KWT", "NZL", "PAK", "QAT", "UKR", "FRA")
        val data_list = ArrayList<model.exchange>()
        var crypto_name = ""
    }


    override fun onBackPressed() {
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

        crypto_name = intent.getStringExtra("name")

        getPrice(crypto_name)

        exchange_swipe.setOnRefreshListener {
            getPrice(crypto_name)
        }
    }

    private fun getURL(fsym: String): String {
        var url = getString(R.string.API_PRICE)
        url += "?fsym=$fsym&tsyms=NGN,AUD,JPY,USD,BWP,BRL,CAD,COP,ESP,KRW,INR,JMD,KWD,NZD,PKR,QAR,UAH,EUR,CNY,GBP"
        return url
    }

    private fun getPrice(fsym: String){
        exchange_swipe.isRefreshing = true
        exchange_list.adapter = null
        data_list.clear()
        val url = getURL(fsym)
        val volley_queue = Volley.newRequestQueue(this@Card)

        val json_object = JsonObjectRequest(Request.Method.GET, url, null, Response.Listener { response ->
            var count = 0
            while(count < currency_array.size){
                val currency = currency_array[count]
                val data = response.getDouble(currency)
                val string_response = String.format("1 $crypto_name = $currency %f", data)
                val data_response = model.exchange(currency, string_response, country_array[count], crypto_name, data)
                data_list.add(data_response)
                count++
            }
            exchange_list.setHasFixedSize(true)

            val layout_manager = LinearLayoutManager(this@Card)
            exchange_list.layoutManager = layout_manager

//            val divider_decoration = DividerItemDecoration(this@Card, layout_manager.orientation)
//            exchange_list.addItemDecoration(divider_decoration)
            exchange_list.adapter = ExchangeAdapter(data_list, this@Card)
            exchange_swipe.isRefreshing = false

        }, Response.ErrorListener { error ->
            exchange_swipe.isRefreshing = false
            error.printStackTrace()
            Toast.makeText(this@Card, "An Internet Error Occurred. Try again", Toast.LENGTH_SHORT).show()
//            Log.e("Volley Error", error.message)
        })
        volley_queue?.add(json_object)
    }
}
