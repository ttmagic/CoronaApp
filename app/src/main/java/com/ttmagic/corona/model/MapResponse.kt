package com.ttmagic.corona.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


data class MapResponse(
    val Data: List<Patient>
)


@Entity
data class Patient(
    @PrimaryKey
    val Id: String,
    val Code: String,      //BN-XX-XX
    val Type: Int?,         //1-->5, F0 --> F5
    val Address: String?,        //Full dia chi.
    val DetectDate: String?,       //Ngay cach ly
    val Lat: String?,       //Lat
    val Lng: String?,       //Lng
    val Locations: List<Location>?,
    val ParentId: String?,      //Id cua parent. Khong co la F0.
    val Visits: String?    //Summaries of List<Location>
)

@Entity
data class Location(
    val Lat: String?,
    val Lng: String?,
    val Timestamp: String?,
    val Visits: String?
)

class Converters {
    @TypeConverter
    fun fromString(value: String?): List<Location>? {
        if (value == null) return null
        val listType: Type = object : TypeToken<List<Location>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: List<Location>?): String? {
        if (list.isNullOrEmpty()) return null
        return Gson().toJson(list)
    }
}


/*
{
            "Id": "2F560D97-C8F5-426C-BC8D-F035B65C52EE",
            "Code": "BN-55",
            "Type": 0,
            "DetectDate": "14/03/2020",
            "Source": null,
            "Address": "Khám sàng lọc tại sân bay nội bài - Cách ly Bệnh viện NĐTƯ 2",
            "IsolateAddress": null,
            "Lat": 21.131486,
            "Lng": 105.774393,
            "Visits": "14/03/2020 : Đến sân bay Nội Bài trên chuyến bay VN0018<br>14/03/2020 : Điều trị tại bệnh viện Nhiệt đới Trung Ương cơ sở 2",
            "Locations": [
                {
                    "Lat": 21.217977,
                    "Lng": 105.792519,
                    "Visits": "Đến sân bay Nội Bài trên chuyến bay VN0018",
                    "Timestamp": "2020-03-14T00:00:00"
                },
                {
                    "Lat": 21.131486,
                    "Lng": 105.774393,
                    "Visits": "Điều trị tại bệnh viện Nhiệt đới Trung Ương cơ sở 2",
                    "Timestamp": "2020-03-14T00:00:00"
                }
            ]
        }
}*/
