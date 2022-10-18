package com.project.flowergarden.entity



data class OwnerEntity(
    val email: String,
    val password: String,
    val storename: String,
    val number: String,
    val address: String,
    val opentime: String,
    val closetime: String,
    val openday: String,
    val x: String,
    val y: String,
    val photo: String
)
{
    constructor() : this("","", "","", "","" ,"","","","","")
}