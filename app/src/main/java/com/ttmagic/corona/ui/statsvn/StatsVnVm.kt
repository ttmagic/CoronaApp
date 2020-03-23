package com.ttmagic.corona.ui.statsvn

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.mvvm.BaseViewModel
import com.base.mvvm.onFailed
import com.base.mvvm.onSucceed
import com.base.util.Pref
import com.base.util.toast
import com.ttmagic.corona.model.StatsVn
import com.ttmagic.corona.model.Summary
import com.ttmagic.corona.repo.local.localDb
import com.ttmagic.corona.repo.network.network
import com.ttmagic.corona.util.Const
import kotlinx.coroutines.launch

class StatsVnVm(app: Application) : BaseViewModel(app) {
    val title = MutableLiveData<String>("Thống kê dịch Covid-19 tại Việt Nam")

    val stats = MutableLiveData<ArrayList<StatsVn>>()

    val summary =
        MutableLiveData<Summary>(Pref.getObj(Const.Pref.SUMMARY_INFO, Summary::class.java))
    val lastUpdate = MutableLiveData<Long>()

    init {
        getStatsVn()
    }

    private fun getStatsVn() = coroutines {
        //Load from local db first.
        val localData = localDb.statsVnDao().getAll()
        if (!localData.isNullOrEmpty()) {
            stats.postValue(localData as ArrayList<StatsVn>)
        }

        //Check if needed to load online.
        val elapsed = System.currentTimeMillis() - Pref.getLong(Const.Pref.LAST_UPDATE_STATS_VN)
        if (elapsed < Const.STATS_UPDATE_INTERVAL) {
            lastUpdate.postValue(Pref.getLong(Const.Pref.LAST_UPDATE_STATS_VN))
            if (!stats.value.isNullOrEmpty()) return@coroutines
        }

        //Load data online
        network().getStatsVn().onSucceed {
            stats.postValue(it.Data as ArrayList<StatsVn>)
            viewModelScope.launch {
                localDb.statsVnDao().addAll(it.Data)
            }
            Pref.putLong(Const.Pref.LAST_UPDATE_STATS_VN, System.currentTimeMillis())
            lastUpdate.postValue(System.currentTimeMillis())
        }.onFailed {
            toast("Get data failed")
        }
    }

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        summary.postValue(Pref.getObj(Const.Pref.SUMMARY_INFO, Summary::class.java))
    }

}