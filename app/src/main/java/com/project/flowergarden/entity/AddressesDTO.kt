package com.project.flowergarden.entity

import com.google.gson.annotations.SerializedName

//addresses 내부에 있는 도로명 주소, 위도, 영문 주소 등 추출하는 공간
data class AddressesDTO(
    val x: String,
    val y: String
)
