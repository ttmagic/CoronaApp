package com.ttmagic.corona.ui.statsworld

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.base.mvvm.BaseViewModel
import com.base.mvvm.onFailed
import com.base.mvvm.onSucceed
import com.base.util.Pref
import com.base.util.toast
import com.ttmagic.corona.model.Info
import com.ttmagic.corona.model.StatsWorld
import com.ttmagic.corona.model.Summary
import com.ttmagic.corona.repo.local.localDb
import com.ttmagic.corona.repo.network.network
import com.ttmagic.corona.util.Const
import kotlinx.coroutines.launch

class StatsWorldVm(app: Application) : BaseViewModel(app) {
    val title = MutableLiveData<String>("Thống kê dịch Covid-19 trên Thế giới")
    val stats = MutableLiveData<ArrayList<StatsWorld>>()

    val summary =
        MutableLiveData<Summary>(Pref.getObj(Const.Pref.SUMMARY_INFO, Summary::class.java))

    val lastUpdate = MutableLiveData<Long>()


    init {
        getStatsWorld()
    }

    private fun getStatsWorld() = coroutines {
        //Load from local db first.
        val localData = localDb.statsWorldDao().getAll()
        if (!localData.isNullOrEmpty()) {
            stats.postValue(localData as ArrayList<StatsWorld>)
        }

        //Check if needed to load online.
        val elapsed = System.currentTimeMillis() - Pref.getLong(Const.Pref.LAST_UPDATE_STATS_WORLD)
        if (elapsed < Const.STATS_UPDATE_INTERVAL) {
            lastUpdate.postValue(Pref.getLong(Const.Pref.LAST_UPDATE_STATS_WORLD))
            if (!stats.value.isNullOrEmpty()) return@coroutines
        }

        //Load data online
        network().getStatsWorld().onSucceed {
            stats.postValue(it.Data as ArrayList<StatsWorld>)
            viewModelScope.launch {
                localDb.statsWorldDao().addAll(it.Data)
                saveSummaryInfo(it.Data[0])
            }
            Pref.putLong(Const.Pref.LAST_UPDATE_STATS_WORLD, System.currentTimeMillis())
            lastUpdate.postValue(System.currentTimeMillis())
        }.onFailed {
            toast("Get data failed")
        }
    }

    private fun saveSummaryInfo(data: StatsWorld) = data.apply {
        val info = Summary(
            world = Info(CasesWorld, DeadWorld, RecoveredWorld),
            vn = Info(CasesVn, DeadVn, RecoveredVn)
        )
        Pref.putObj(Const.Pref.SUMMARY_INFO, info)
        summary.postValue(info)
    }

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        summary.postValue(Pref.getObj(Const.Pref.SUMMARY_INFO, Summary::class.java))
    }
}