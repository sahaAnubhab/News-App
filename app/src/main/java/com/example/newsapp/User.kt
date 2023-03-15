package com.example.newsapp

import android.net.Uri

data class User(
    val uid: String,
    val email:  String,
    val name: String,
    val phone: String,
    val userTag: String,
    val profilePicLink: Uri
)