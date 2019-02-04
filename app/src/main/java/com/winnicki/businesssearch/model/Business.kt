package com.winnicki.businesssearch.model

import com.google.gson.annotations.SerializedName

data class Business(
    val id: String,
    val name: String,
    val rating: Float,
    val distance: Float,
    val categories: List<Category>,
    val location: Address? = null,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("display_phone")
    val phone: String
) {

    data class Category(
        val alias: String,
        val title: String
    )
}
