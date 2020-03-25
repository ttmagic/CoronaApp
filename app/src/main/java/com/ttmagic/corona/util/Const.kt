package com.ttmagic.corona.util

import com.google.android.gms.maps.model.LatLng

object Const {
    const val DB_NAME = "com.ttmagic.corona"
    const val BASE_URL = "http://smartcity.hanoi.gov.vn/Home/"
    val hanoi = LatLng(21.028511, 105.804817)

    const val STATUS_F0 = 0
    const val STATUS_F1 = 1
    const val STATUS_F2 = 2
    const val STATUS_F3 = 3

    object Pref {
        const val LAST_USER_POSITION = "LAST_USER_POSITION"
        const val SUMMARY_INFO = "SUMMARY_INFO"
    }

    object Bus {
        const val GPS = "gps"
    }
}