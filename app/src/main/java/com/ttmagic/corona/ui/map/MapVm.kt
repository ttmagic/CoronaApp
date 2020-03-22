package com.ttmagic.corona.ui.map

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.mvvm.BaseViewModel
import com.base.mvvm.onFailed
import com.base.mvvm.onSucceed
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
        this.filtersChecked = filter
        if (patients.isNullOrEmpty()) return

        val list = arrayListOf<Patient>()
        if (filtersChecked[0]) {    //F0
            list.addAll(patients!!.filter { it.Status == 1 })
        }
        if (filtersChecked[1]) {    //F1
            list.addAll(patients!!.filter { it.Status == 2 })
        }
        if (filtersChecked[2]) {    //F2
            list.addAll(patients!!.filter { it.Status == 3 })
        }
        if (filtersChecked[3]) {    //F3/4/5
            list.addAll(patients!!.filter { it.Status != null && it.Status >= 4 })
        }
        listDisplay.postValue(list)
    }
}