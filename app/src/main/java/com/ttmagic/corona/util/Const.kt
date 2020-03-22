package com.ttmagic.corona.util

import com.google.android.gms.maps.model.LatLng

object Const {
    const val DB_NAME = "com.ttmagic.corona"
    const val BASE_URL = "http://smartcity.hanoi.gov.vn/Home/"
    val hanoi = LatLng(21.028511, 105.804817)

    const val MAP_UPDATE_INTERVAL = 3 * 60 * 60 * 1000      //3 Hours
    const val STATS_UPDATE_INTERVAL = 1 * 60 * 60 * 1000    //1 Hours


    object Pref {
        const val LAST_USER_POSITION = "LAST_USER_POSITION"
        const val LAST_UPDATE_MAP = "LAST_UPDATE_MAP"
        const val LAST_UPDATE_STATS_VN = "LAST_UPDATE_STATS_VN"
        const val LAST_UPDATE_STATS_WORLD = "LAST_UPDATE_STATS_WORLD"

        const val SUMMARY_INFO = "SUMMARY_INFO"
    }

    object Bus {
        const val GPS = "gps"
    }
}