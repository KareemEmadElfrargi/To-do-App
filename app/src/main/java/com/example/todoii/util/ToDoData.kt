package com.example.todoii.util

data class ToDoData(
    val taskId:String,
    var task:String,
    var description:String,
    var timeCreated:String = "",
)