package com.example.retrofit_practice.data

import com.google.gson.annotations.SerializedName

data class Dust(val response: DustResponse)

// 변수명은 원래 서버에서 사용하는 값과 똑같이 작성
data class DustResponse(
    // 만약 앱 내에서 다른 변수명으로 사용하고 싶다면 아래 코드처럼
    // '@SerializedName("서버에서 변수명") val 앱내변수명:자료형' 을 사용
    @SerializedName("body")
    val dustBody: DustBody,
    @SerializedName("header")
    val dustHeader: DustHeader
)

data class DustBody(
    val totalCount: Int,
    @SerializedName("items")
    val dustItem: MutableList<DustItem>?,
    val pageNo: Int,
    val numOfRows: Int
)

data class DustHeader(
    val resultCode: String,
    val resultMsg: String
)

data class DustItem(
    val so2Grade: String,
    val coFlag: String?,
    val khaiValue: String,
    val so2Value: String,
    val coValue: String,
    val pm25Flag: String?,
    val pm10Flag: String?,
    val o3Grade: String,
    val pm10Value: String,
    val khaiGrade: String,
    val pm25Value: String,
    val sidoName: String,
    val no2Flag: String?,
    val no2Grade: String,
    val o3Flag: String?,
    val pm25Grade: String,
    val so2Flag: String?,
    val dataTime: String,
    val coGrade: String,
    val no2Value: String,
    val stationName: String,
    val pm10Grade: String,
    val o3Value: String
)