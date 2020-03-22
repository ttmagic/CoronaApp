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
    val AccountId: String?,
    val Address: String?,        //Full dia chi.
    val Age: String?,           //Tuoi
    val FullName: String?,       //Ten Benh nhan
    val Gender: String?,
    val IdentificationCard: String?,
    val IsolateAddress: String?,
    val IsolateDate: String?,       //Ngay cach ly
    val Level: Int,
    val LocationLat: String?,       //Lat
    val LocationLng: String?,       //Lng
    val Locations: List<Location>?,
    val National: String?,
    val ParentId: String?,      //Id cua parent. Khong co la F0.
    val PhoneNumber: String?,
    val Status: Int?,         //0-->5, F0 --> F5
    val Title: String,      //BN-XX-XX
    val UnFollow: Boolean?,
    val Visits: String?    //Summaries of List<Location>
)

@Entity
data class Location(
    val id: String?,
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
    "ParentId": null,
    "Title": "BN-51",
    "FullName": "Tạ Phương Thảo",
    "Age": "39",
    "Gender": "nữ",
    "Address": "Tòa T3 0602 số 3 Lương Yên - Hiện đang ở bệnh viện nhiệt đới TW CS 2",
    "Ward": "Bạch Đằng",
    "District": "Hai Bà Trưng",
    "City": "Hà Nội",
    "National": "",
    "PhoneNumber": "903230304",
    "IdentificationCard": "",
    "Element": "",
    "IsolateDate": "3/14/2020",
    "IsolateType": "",
    "IsolateAddress": "",
    "LocationLat": "21.0284956",
    "LocationLng": "105.8232054",
    "Status": 1,
    "UnFollow": false,
    "UnFollowDate": null,
    "Name": null,
    "InfectedDay": null,
    "Type": null,
    "State": null,
    "Home": null,
    "Visits": "Đến 200 Nguyễn Sơn, Long Biên<br>Đến Paris Pháp<br>Đến Nhà hàng Lộc Vừng, Long BIên<br>Đến 20 Núi Trúc, Ba Đình<br>Điều trị tại bệnh viện Nhiệt đới Trung Ương cơ sở 2<br>Đến 200 Nguyễn Sơn, Long Biên<br>Đến 20 Núi Trúc, Ba Đình<br>Đến sân bay Nội Bài<br>Đến khách Sạn Hà Nội, Giảng Võ",
    "Locations": [
    {
        "Lat": 21.04487,
        "Lng": 105.882149,
        "Visits": "Đến 200 Nguyễn Sơn, Long Biên",
        "Timestamp": "3/10/2020"
    },
    {
        "Lat": 48.85718,
        "Lng": 2.34141,
        "Visits": "Đến Paris Pháp",
        "Timestamp": "3/4/2020"
    },
    {
        "Lat": 21.029916,
        "Lng": 105.826778,
        "Visits": "Đến Nhà hàng Lộc Vừng, Long BIên",
        "Timestamp": "3/11/2020"
    },
    {
        "Lat": 21.028225,
        "Lng": 105.824257,
        "Visits": "Đến 20 Núi Trúc, Ba Đình",
        "Timestamp": "3/11/2020"
    },
    {
        "Lat": 21.131486,
        "Lng": 105.774393,
        "Visits": "Điều trị tại bệnh viện Nhiệt đới Trung Ương cơ sở 2",
        "Timestamp": "3/13/2020"
    },
    {
        "Lat": 21.04487,
        "Lng": 105.882149,
        "Visits": "Đến 200 Nguyễn Sơn, Long Biên",
        "Timestamp": "3/11/2020"
    },
    {
        "Lat": 21.028225,
        "Lng": 105.824257,
        "Visits": "Đến 20 Núi Trúc, Ba Đình",
        "Timestamp": "3/10/2020"
    },
    {
        "Lat": 21.21401,
        "Lng": 105.79823,
        "Visits": "Đến sân bay Nội Bài",
        "Timestamp": "3/10/2020"
    },
    {
        "Lat": 21.04487,
        "Lng": 105.882149,
        "Visits": "Đến khách Sạn Hà Nội, Giảng Võ",
        "Timestamp": "3/12/2020"
    }
    ],
    "Relation": null,
    "Level": 0,
    "StatusText": null,
    "AccountId": "",
    "Id": "06A07E21-9E67-449D-9C97-0C87D7F3ADF6",
    "CustomProperties": {}
}*/
