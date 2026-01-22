package com.example.poolcontrol.data.local.user

import  androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

//anotacion para indicar que es tabla de BD
@Entity(tableName = "users")

data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L, //valor inicial L-> LONG
    val name: String,
    val lastname: String,
    val email: String,
    val password: String,
    val phone: Number,
    //agregar llave foranea
    //val rol: Int
)