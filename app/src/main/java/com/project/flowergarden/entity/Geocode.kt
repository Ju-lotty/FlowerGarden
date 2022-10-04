package com.project.flowergarden.entity

import com.google.gson.annotations.SerializedName

//Send 후 검색 값 중에 추출할 값 입력
data class Geocode(
    //@SerializedName Postman의 결과 파라미터 값을 매칭
    //응답 바디
    @SerializedName("status")val status: String, //검색 결과 상태 코드, 필수여부 Y
    // 응답 버디 중 원하는 값 출력, 필수여부 N
    @SerializedName("addresses")val addresses: Array<AddressesDTO>
)
