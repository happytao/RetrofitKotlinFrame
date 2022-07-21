package com.example.kotlinflowlearn.logutil

import android.util.Log

fun <T :Throwable?> T.logE(tag:String = "xtxt") =
    this?.let { Log.e("xtxt",it.stackTraceToString()) }