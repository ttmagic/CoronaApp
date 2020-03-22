package com.ttmagic.corona.model

import com.base.mvvm.Entity


data class Province(
    val Confirmed: String,
    val Deaths: String,
    val Province_Name: String,
    val Recovered: String
) : Entity() {
    override val uniqueId: Any
        get() = Province_Name
}

