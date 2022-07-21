package com.example.kotlinflowlearn.api

import okhttp3.OkHttpClient

object OkHttpWrapper {
    val okHttpClient:OkHttpClient by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { getInstance() }

    private fun getInstance():OkHttpClient {
        val client = OkHttpClient.Builder()
            .build()
        return client
    }
}