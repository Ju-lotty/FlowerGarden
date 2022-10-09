package com.project.flowergarden.entity


data class OwnerEntity(
    val id: String,
    val pw: String,
    val storename: String,
    val number: String,
    val opentime: String,
    val closetime: String,
    val address: String,
    val x: String,
    val y: String
)
{
    constructor() : this("","", "","", "","" ,"","","")
}