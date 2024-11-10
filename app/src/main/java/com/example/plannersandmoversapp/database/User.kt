package com.example.plannersandmoversapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val username: String,
    val email: String,
    val phone :Int,
    val password:String,
    val confirm: String
)