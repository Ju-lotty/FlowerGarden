package com.project.flowergarden.entity

data class UserEntity (
    val id: String,
    val pw: String,
    val nickname: String
)
{
    constructor() : this("","", "")
}