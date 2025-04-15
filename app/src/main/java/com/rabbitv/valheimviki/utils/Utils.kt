package com.rabbitv.valheimviki.utils


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import retrofit2.Response

fun <T> Response<List<T>>.bodyList(): List<T> {
    return body() ?: emptyList()
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}


