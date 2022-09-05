package com.project.flowergarden.entity


data class OwnerEntity (
    val id: String,
    val pw: String,
    val storename: String
)
{
    constructor() : this("","", "")
}