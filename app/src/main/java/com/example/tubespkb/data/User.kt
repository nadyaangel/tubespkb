package com.example.tubespkb.data

data class User(
    val name: String,
    val email: String,
    var imagePath: String = ""
){
    constructor(): this("", "", "")
}