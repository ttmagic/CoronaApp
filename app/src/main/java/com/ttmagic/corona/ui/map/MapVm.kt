package com.ttmagic.corona.ui.map

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.mvvm.BaseViewModel
import com.base.mvvm.onFailed
import com.base.mvvm.onSucceed
import com.base.util.Logger
import com.base.util.Pref
import com.base.util.toast
import com.google.android.gms.maps.model.CameraPosition
import com.ttmagic.corona.model.Patient
import com.ttmagic.corona.repo.local.localDb
import com.ttmagic.corona.repo.network.network
import com.ttmagic.corona.util.Const
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MapVm(app: Application) : BaseViewModel(app) {
    val filters = arrayOf("F0", "F1", "F2", "F3/4/5")
    var filtersChecked = booleanArrayOf(true, true, true, true)
        private set

    var lastCamPos: CameraPosition? = null

    private var patients: List<Patient>? = null         //Original data
    val listDisplay = MutableLiveData<List<Patient>>()  //Filtered data for display
    val f0 = MutableLiveData(0)
    val f1 = MutableLiveData(0)
    val f2 = MutableLiveData(0)
    val f345 = MutableLiveData(0)
    val lastUpdate = MutableLiveData<Long>()

    init {
        getAllPatients()
    }

    private fun getAllPatients() = coroutines {
        delay(1200)  //Delay for map init and zoom.

        //Load from local db first.
        val localData = localDb.userDao().getAll()
        if (!localData.isNullOrEmpty()) {
            patients = localData
            listDisplay.postValue(patients)
            applyFilter()
        }

        //Check if needed to load online.
        val elapsed = System.currentTimeMillis() - Pref.getLong(Const.Pref.LAST_UPDATE_MAP)
        if (elapsed < Const.MAP_UPDATE_INTERVAL) {
            lastUpdate.postValue(Pref.getLong(Const.Pref.LAST_UPDATE_MAP))
            if (!patients.isNullOrEmpty()) return@coroutines
        }

        //Load data online.
        network().getPatientMap().onSucceed { res ->
            patients = res.Data.filter { it.Status != null }
            viewModelScope.launch {
                localDb.userDao().addAll(patients ?: arrayListOf())
            }
            Pref.putLong(Const.Pref.LAST_UPDATE_MAP, System.currentTimeMillis())
            listDisplay.postValue(patients)
            lastUpdate.postValue(System.currentTimeMillis())
        }.onFailed {
            toast("Get data failed")
        }
    }

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        applyFilter()
    }

    fun applyFilter(filter: BooleanArray = filtersChecked) {
        Logger.d("Apply filter")
        this.filtersChecked = filter
        if (patients.isNullOrEmpty()) return

        val list = arrayListOf<Patient>()
        if (filtersChecked[0]) {    //F0
            val f0s = patients!!.filter { it.Status == 1 }
            f0.postValue(f0s.size)
            list.addAll(f0s)
        }
        if (filtersChecked[1]) {    //F1
            val f1s = patients!!.filter { it.Status == 2 }
            f1.postValue(f1s.size)
            list.addAll(f1s)
        }
        if (filtersChecked[2]) {    //F2
            val f2s = patients!!.filter { it.Status == 3 }
            f2.postValue(f2s.size)
            list.addAll(f2s)
        }
        if (filtersChecked[3]) {    //F3/4/5
            val f345s = patients!!.filter { it.Status != null && it.Status >= 4 }
            f345.postValue(f345s.size)
            list.addAll(f345s)
        }
        listDisplay.postValue(list)
    }
}