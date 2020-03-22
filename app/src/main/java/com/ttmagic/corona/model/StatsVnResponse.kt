package com.ttmagic.corona.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class StatsVnResponse(
    val Data: List<StatsVn>
)


@Entity
data class StatsVn(
    val Cases: String,
    val City: String,
    val Deaths: String,
    @PrimaryKey
    val Id: String
): com.base.mvvm.Entity() {
    override val uniqueId: Any
        get() = Id
}

//        {
//            "City": "Hà Nội",
//            "Cases": "26",
//            "Deaths": "0",
//            "Id": "7442f5b4-2b51-4d7b-b1a4-5b8078f490c6",
//            "CustomProperties": {}
//        },