package com.yung_coder.oluwole.alc_assessement.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.yung_coder.oluwole.alc_assessement.model.model
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.yung_coder.oluwole.alc_assessement.Card
import com.yung_coder.oluwole.alc_assessement.R

/**
 * Created by yung on 10/4/17.
 */

class CryptoAdapter constructor(data_list: List<model.model>?, context: Context?): RecyclerView.Adapter<CryptoAdapter.DataViewAdapter>() {

    var data_list: List<model.model>? = null
    var context:Context? = null

    init {
        this.data_list = data_list
        this.context = context
    }

    override fun getItemCount(): Int {
        return data_list?.count() ?: 0
    }

    override fun onBindViewHolder(holder: DataViewAdapter, position: Int) {
        val data = data_list?.get(position)

        Glide.with(context).load(data?.image_url).into(holder.currency_image)

        holder.currency_name.text = data?.full_name

        holder.currency_item.setOnClickListener {
            val intent = Intent(context, Card::class.java)
            intent.putExtra("name", data?.name)
            context?.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DataViewAdapter{
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.cryto_list_view, parent, false)
        val view_holder = DataViewAdapter(view)
        context = view.context
        return view_holder
    }

    class DataViewAdapter(layoutView: View): RecyclerView.ViewHolder(layoutView){
        var currency_image: ImageView = layoutView.findViewById(R.id.image_crypto)
        var currency_name: TextView = layoutView.findViewById(R.id.text_name)
        val currency_item: RelativeLayout = layoutView.findViewById(R.id.crypto_item)
    }
}