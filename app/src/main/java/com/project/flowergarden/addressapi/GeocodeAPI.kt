package com.project.flowergarden.addressapi


import com.project.flowergarden.entity.Geocode
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

//GET형식 API : 데이터 요청시 서버에서 반환해주는 https형식
//https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode

interface GeocodeAPI {
    @GET("/map-geocode/v2/geocode") //Base url을 제외한 주소
    fun getAddress(
        //Params 추가, 필수 입력 값 : 주소
        @Query("query") query : String,
        @Header("X-NCP-APIGW-API-KEY-ID") clientId: String,
        @Header("X-NCP-APIGW-API-KEY") clientSecret: String
    ): Call<Geocode> // TODO: 반환형은 모델 데이터
}

//Interface 구현체이기 때문에 JoinOwner에서 retrofit.create 추가