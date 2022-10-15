package com.project.flowergarden.entity

data class UserEntity (
    val email: String,
    val password: String,
    val nickname: String
)
{
    constructor() : this("","", "")
}