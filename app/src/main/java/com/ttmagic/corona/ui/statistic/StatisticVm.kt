package com.ttmagic.corona.ui.statistic

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.base.mvvm.BaseViewModel
import com.base.mvvm.onSucceed
import com.ttmagic.corona.model.Province
import com.ttmagic.corona.repo.network.network
import com.ttmagic.corona.util.Const

class StatisticVm(app: Application) : BaseViewModel(app) {

    val listProvinces = MutableLiveData<List<Province>>()

    val totalConfirmed = Transformations.map(listProvinces) { it.sumBy { it.Confirmed.toInt() } }

    val totalRecovered = Transformations.map(listProvinces) { it.sumBy { it.Recovered.toInt() } }

    init {
        getListProvinces()
    }

    fun getListProvinces() = coroutines {
        network().getProvinces(Const.provinceQuery).onSucceed {
            listProvinces.value = it.data.provinces
        }
    }
}