package com.project.flowergarden.entity


data class OwnerEntity(
    val id: String,
    val pw: String,
    val storename: String,
    val address: String,
    val x: String,
    val y: String
)
{
    constructor() : this("","", "","", "","" )
}