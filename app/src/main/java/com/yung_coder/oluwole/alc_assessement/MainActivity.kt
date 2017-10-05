package com.yung_coder.oluwole.alc_assessement

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.yung_coder.oluwole.alc_assessement.adapters.CryptoAdapter
import com.yung_coder.oluwole.alc_assessement.model.model
import hollowsoft.country.Country
import hollowsoft.country.extension.all
import org.json.JSONException
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        var IMAGE_BASE_URL: String = ""
        var LINK_BASE_URL: String = ""
        val my_list = ArrayList<model.model>()
    }

    private var crypto_view: RecyclerView? = null
    private var volley_queue: RequestQueue? = null
    private var swipe_refresh: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipe_refresh = findViewById(R.id.swipe_refresh)
        swipe_refresh?.setOnRefreshListener{
            loadCrypto()
        }
        loadCrypto()

//        val country = Country.all(Locale.ENGLISH)
//        for (item in country) {
//            Log.e("Country", item.id)
//        }
    }

    fun loadCrypto() {
        swipe_refresh?.isRefreshing = true
        val URL = getString(R.string.API_URL)
        volley_queue = Volley.newRequestQueue(this@MainActivity)
        Log.d("URL", URL)

        val jsonObjReq = JsonObjectRequest(Request.Method.GET, URL, null,
                Response.Listener { response ->
                    val response_message = response.getString("Response")
                    if (response_message == "Success") {
                        IMAGE_BASE_URL = response.getString("BaseImageUrl")
                        LINK_BASE_URL = response.getString("BaseLinkUrl")

                        val returned_data = response.getJSONObject("Data")
                        val keys = returned_data.keys()
                        while (keys.hasNext()) {
                            val key = keys.next().toString()
                            val single_data = returned_data.getJSONObject(key)
                            try {
                                val check = single_data.getString("Name")
                                if(check == "BTC" || check == "ETH") {
                                    val data = model.model(single_data.getString("Name"), LINK_BASE_URL + single_data.getString("Url"),
                                            single_data.getString("FullName"), IMAGE_BASE_URL + single_data.getString("ImageUrl"), single_data.getString("Id"))
                                    my_list.add(data)
                                }
                            } catch (e: JSONException) {
                                e.printStackTrace()
                                Log.e("Error", e.message)
                            }
                        }
                        crypto_view = findViewById(R.id.currency_list)
                        crypto_view?.adapter = null
                        crypto_view?.setHasFixedSize(true)

                        val layout_manager = LinearLayoutManager(this@MainActivity)
                        crypto_view?.layoutManager = layout_manager

                        val divider_decoration = DividerItemDecoration(this@MainActivity, layout_manager.orientation)
                        crypto_view?.addItemDecoration(divider_decoration)
                        crypto_view?.adapter = CryptoAdapter(my_list, this@MainActivity)
                        swipe_refresh?.isRefreshing = false
                    } else {
                        Log.e("Error", "Error Message : " + response.getString("Message"))
                    }
                },
                Response.ErrorListener { error ->
                    swipe_refresh?.isRefreshing = false
                    error.printStackTrace()
                    Toast.makeText(this@MainActivity, "An Internet Error Occurred. Try again", Toast.LENGTH_SHORT).show()
                })
        volley_queue?.add(jsonObjReq)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        val search_item = menu?.findItem(R.id.action_search)
        val search_view = MenuItemCompat.getActionView(search_item) as SearchView
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(query: String?): Boolean {
                val filtered_list = filter(my_list, query)
                crypto_view?.adapter = null
                crypto_view?.adapter = CryptoAdapter(filtered_list, this@MainActivity)
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
    }

    private fun filter(list: ArrayList<model.model>, query: String?): List<model.model>? {
        val lower_query = query?.toLowerCase()
        val new_list: ArrayList<model.model> = ArrayList()
        if(list != null){
            for(item in list){
                val text = item.full_name.toLowerCase()
                if(text.contains(lower_query.toString())){
                    new_list.add(item)
                }
            }
        }
        return new_list
    }

}
