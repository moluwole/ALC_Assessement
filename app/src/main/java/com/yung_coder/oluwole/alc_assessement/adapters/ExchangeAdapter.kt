package com.yung_coder.oluwole.alc_assessement.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.yung_coder.oluwole.alc_assessement.Conversion
import com.yung_coder.oluwole.alc_assessement.R
import com.yung_coder.oluwole.alc_assessement.model.model
import hollowsoft.country.Country
import hollowsoft.country.extension.all
import hollowsoft.country.extension.image
import java.util.*

/**
 * Created by yung on 10/4/17.
 */
class ExchangeAdapter constructor(data_list:List<model.exchange>, context: Context): RecyclerView.Adapter<ExchangeAdapter.DataAdapter>(){

    private var data_list: List<model.exchange>? = null
    private var context: Context? = null

    init {
        this.data_list = data_list
        this.context = context
    }

    override fun getItemCount(): Int {
        return data_list?.count() ?: 0
    }

    override fun onBindViewHolder(holder: DataAdapter?, position: Int) {
        val data = data_list?.get(position)
        holder?.currency_name?.text = data?.country
        holder?.exchange_rate?.text = data?.exchange

        val image = Country.all(Locale.ENGLISH)
        image
                .filter { it.id == data?.country_id }
                .forEach { holder?.crypto_image?.setImageResource(it.image) }

        holder?.exchange_card?.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("crypto_value", data?.crypto_name)
            bundle.putString("currency_value", data?.country)
            bundle.putDouble("rate", data?.rate!!)

            val intent = Intent(context, Conversion::class.java)
            intent.putExtra("extra", bundle)
            context?.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DataAdapter {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.exchange_list_view, parent, false)
        val view_holder = DataAdapter(view)
        context = view.context
        return view_holder
    }

    class DataAdapter(layoutView: View): RecyclerView.ViewHolder(layoutView){
        val currency_name: TextView = layoutView.findViewById(R.id.exchange_country)
        val exchange_rate: TextView = layoutView.findViewById(R.id.text_exchange)
        val crypto_image: ImageView = layoutView.findViewById(R.id.exchange_crypto)
        val exchange_card: CardView = layoutView.findViewById(R.id.exchange_card)
    }
}