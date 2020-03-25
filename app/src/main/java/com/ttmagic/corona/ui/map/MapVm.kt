package com.ttmagic.corona.ui.map

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.mvvm.BaseViewModel
import com.base.mvvm.onSucceed
import com.base.util.Logger
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ttmagic.corona.model.Patient
import com.ttmagic.corona.repo.local.localDb
import com.ttmagic.corona.repo.network.network
import com.ttmagic.corona.util.Const
import com.ttmagic.corona.util.formatDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MapVm(app: Application) : BaseViewModel(app) {
    val filters = arrayOf("F0", "F1", "F2", "F3/4/5", "F0 đã đến")
    var filtersChecked = booleanArrayOf(true, true, true, true, true)
        private set

    var lastCamPos: CameraPosition? = null

    private var patients: List<Patient>? = null         //Original data
    val listMarkers = MutableLiveData<List<MarkerOptions>>()
    val f0 = MutableLiveData(0)
    val f1 = MutableLiveData(0)
    val f2 = MutableLiveData(0)

    val lastUpdate = MutableLiveData<Long>()

    init {
        getAllPatients()
    }

    private fun getAllPatients() = coroutines {
        delay(800)  //Delay for map init and zoom.

        //Load from local db first.
        val localData = localDb.userDao().getAll()
        if (!localData.isNullOrEmpty()) {
            patients = localData
            applyFilter()
        }

        //Load data online.
        network().getPatientMap().onSucceed { res ->
            patients = res.Data.filter { it.Type != null }
            if (patients.isNullOrEmpty()) return@onSucceed
            viewModelScope.launch {
                localDb.userDao().addAll(patients!!)
            }
            createMarkers(patients!!)
            lastUpdate.postValue(System.currentTimeMillis())
        }
    }

    fun applyFilter(filter: BooleanArray = filtersChecked) = viewModelScope.launch(Dispatchers.IO) {
        filtersChecked = filter
        if (patients.isNullOrEmpty()) return@launch

        val list = arrayListOf<Patient>()
        if (filtersChecked[0] || filtersChecked[4]) {    //F0, F0 visited places.
            val f0s = patients!!.filter { it.Type == Const.STATUS_F0 }
            f0.postValue(f0s.size)
            list.addAll(f0s)
        }
        if (filtersChecked[1]) {    //F1
            val f1s = patients!!.filter { it.Type == Const.STATUS_F1 }
            f1.postValue(f1s.size)
            list.addAll(f1s)
        }
        if (filtersChecked[2]) {    //F2
            val f2s = patients!!.filter { it.Type == Const.STATUS_F2 }
            f2.postValue(f2s.size)
            list.addAll(f2s)
        }
        if (filtersChecked[3]) {    //F3/4/5
            val f345s = patients!!.filter { it.Type != null && it.Type >= Const.STATUS_F3 }
            list.addAll(f345s)
        }
        createMarkers(list)
    }

    private fun createMarkers(list: List<Patient>) = viewModelScope.launch(Dispatchers.Default) {
        Logger.d("createMarkers list size = ${list.size}")
        val markers = arrayListOf<MarkerOptions>()
        list.forEach {
            val lat = it.Lat?.toDoubleOrNull()
            val lng = it.Lng?.toDoubleOrNull()
            if (lat != null && lng != null) {
                val icon = when (it.Type) {
                    Const.STATUS_F0 -> com.ttmagic.corona.ui.map.f0
                    Const.STATUS_F1 -> com.ttmagic.corona.ui.map.f1
                    Const.STATUS_F2 -> com.ttmagic.corona.ui.map.f2
                    else -> f3
                }
                val title = "Trường hợp: ${it.Code}"
                var snippet = "Địa chỉ: ${it.Address}"
                if (!it.DetectDate.isNullOrBlank()) {
                    snippet += "\nNgày phát hiện: ${it.DetectDate}"
                }
                if (!it.Visits.isNullOrBlank()) {
                    snippet += "\nLộ trình:\n${it.Visits.replace(
                        "<br>",
                        "\n".replace("</br>", "")
                    )}"
                }

                val marker = MarkerOptions().position(LatLng(lat, lng))
                    .icon(icon)
                    .title(title)
                    .snippet(snippet)

                if (it.Type == Const.STATUS_F0 && !filtersChecked[0]) {
                    //Do nothing when not display F0 but item is F0.
                } else {
                    markers.add(marker)
                }

                if (it.Type == Const.STATUS_F0 && filtersChecked[4]) {    //Only display places of F0 when setting true.
                    it.Locations?.forEach { place ->
                        val placeLat = place.Lat?.toDoubleOrNull()
                        val placeLng = place.Lng?.toDoubleOrNull()
                        if (placeLat != null && placeLng != null) {
                            val mark = MarkerOptions().position(LatLng(placeLat, placeLng))
                                .icon(f0Visited)
                                .title(title)
                                .snippet(place.Timestamp?.formatDate("yyyy-MM-dd'T'HH:mm:ss") + ": " + place.Visits)
                            markers.add(mark)
                        }
                    }
                }
            }
        }
        listMarkers.postValue(markers)
    }

}