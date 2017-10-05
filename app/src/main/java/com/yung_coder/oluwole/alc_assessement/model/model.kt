package com.yung_coder.oluwole.alc_assessement.model

import hollowsoft.country.Country

/**
 * Created by yung on 10/4/17.
 */
class model {
    class model(val name: String, val url: String, val full_name: String, val image_url: String, val _id: String)

    class exchange(val country: String, val exchange: String, val country_id: String, val crypto_name: String, val rate: Double)
}