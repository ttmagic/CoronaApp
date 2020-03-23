package com.ttmagic.corona.ui.statsvn

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.mvvm.BaseViewModel
import com.base.mvvm.onSucceed
import com.base.util.Pref
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

    fun getStatsVn() = coroutines {
        //Load from local db first.
        val localData = localDb.statsVnDao().getAll()
        if (!localData.isNullOrEmpty()) {
            stats.postValue(localData as ArrayList<StatsVn>)
        }

        //Load data online
        network().getStatsVn().onSucceed {
            if (it.Data.isNullOrEmpty()) return@onSucceed
            stats.postValue(it.Data as ArrayList<StatsVn>)
            viewModelScope.launch {
                localDb.statsVnDao().addAll(it.Data)
            }
            lastUpdate.postValue(System.currentTimeMillis())
        }
    }

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        summary.postValue(Pref.getObj(Const.Pref.SUMMARY_INFO, Summary::class.java))
    }

    override fun onCoroutinesExceptions(error: Exception) {
        super.onCoroutinesExceptions(error)
        setLoading(false)
    }

}