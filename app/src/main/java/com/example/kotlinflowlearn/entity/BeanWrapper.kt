package com.example.kotlinflowlearn.entity

data class BeanWrapper<T>(
    var errorCode: Int,
    var reason:String,
    var result:T
) {
}