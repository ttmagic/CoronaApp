package com.ttmagic.corona.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.base.mvvm.BaseFragment
import com.base.util.*
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.ttmagic.corona.BR
import com.ttmagic.corona.R
import com.ttmagic.corona.databinding.FragmentMapBinding
import com.ttmagic.corona.model.Patient
import com.ttmagic.corona.util.Const
import com.ttmagic.corona.util.GpsUtils
import com.ttmagic.corona.util.formatDate
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.my_info_window.view.*
import kotlinx.coroutines.launch

class MapFragment : BaseFragment<MapVm, FragmentMapBinding>(R.layout.fragment_map),
    OnMapReadyCallback {
    override fun brVariableId(): Int = BR.viewModel
    override fun isFragmentScopeViewModel(): Boolean = false

    private val gpsUtil by lazy { GpsUtils(requireActivity()) }
    private lateinit var mMap: GoogleMap
    private var mLastUserPos: LatLng? =
        Pref.getObj(Const.Pref.LAST_USER_POSITION, LatLng::class.java)

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        mMap.setInfoWindowAdapter(MyInfoWindow(requireContext()))
        mMap.uiSettings.apply {
            isCompassEnabled = false
            isMapToolbarEnabled = false
            isRotateGesturesEnabled = false
            isTiltGesturesEnabled = false
        }
        animateCameraMapView(false)
    }

    override fun initView(binding: FragmentMapBinding) {
        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)?.getMapAsync(this)
        checkLocationPermission(manually = false)

        btnMyLocation.setOnClickListener {
            checkLocationPermission(manually = true)
        }

        btnFilter.onClick {
            showFilterDialog()
        }
    }

    override fun observeData() {
        super.observeData()
        Bus.get(Const.Bus.GPS).observe(viewLifecycleOwner, Observer {
            addCurrPosMarker()
            animateCameraMapView(true)
        })

        viewModel.listDisplay.observe {
            Logger.d("Observe: ${it?.size}")
            it?.let {
                lifecycleScope.launch {
                    inflateOnMap(it)
                }
            }
        }
    }

    /**
     * Display corresponding list patient on map.
     */
    private fun inflateOnMap(list: List<Patient>) {
        val f0Visited = BitmapDescriptorFactory.fromResource(R.drawable.ic_place)
        val f0 = BitmapDescriptorFactory.fromResource(R.drawable.ic_f0)
        val f1 = BitmapDescriptorFactory.fromResource(R.drawable.ic_f1)
        val f2 = BitmapDescriptorFactory.fromResource(R.drawable.ic_f2)
        val f3 = BitmapDescriptorFactory.fromResource(R.drawable.ic_f3)

        val markers = arrayListOf<MarkerOptions>()
        list.forEach {
            val lat = it.LocationLat?.toDoubleOrNull()
            val lng = it.LocationLng?.toDoubleOrNull()
            if (lat != null && lng != null) {
                val icon = when (it.Status) {
                    Const.STATUS_F0 -> f0
                    Const.STATUS_F1 -> f1
                    Const.STATUS_F2 -> f2
                    else -> f3
                }
                val title = "Trường hợp: ${it.Title}"
                var snippet = "Địa chỉ: ${it.Address}"
                if (!it.IsolateDate.isNullOrBlank()) {
                    snippet += "\nNgày cách ly: ${it.IsolateDate.formatDate("MM/dd/yyyy")}"
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

                if (it.Status == Const.STATUS_F0 && !viewModel.filtersChecked[0]) {
                    //Do nothing when not display F0 but item is F0.
                } else {
                    markers.add(marker)
                }

                if (it.Status == Const.STATUS_F0 && viewModel.filtersChecked[4]) {    //Only display places of F0 when setting true.
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
        if (::mMap.isInitialized) {
            mMap.clear()
            addCurrPosMarker()
            markers.forEach { mMap.addMarker(it) }
        }
    }

    private fun showFilterDialog() {
        val temp = viewModel.filtersChecked.clone()
        AlertDialog.Builder(requireContext())
            .setTitle("Bộ lọc hiển thị")
            .setMultiChoiceItems(viewModel.filters, temp) { _, pos, checked ->
                temp[pos] = checked
            }.setPositiveButton("Áp dụng") { dialog, _ ->
                viewModel.applyFilter(temp)
                dialog.dismiss()
            }.show()
    }

    private fun checkLocationPermission(manually: Boolean) {
        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION) { granted ->
            if (granted) {
                gpsUtil.turnGPSOn {
                    addCurrPosMarker()
                    animateCameraMapView(manually)
                }
            }
        }
    }

    /**
     * Update and animate camera follow user's position.
     */
    @SuppressLint("MissingPermission")
    private fun addCurrPosMarker() {
        val marker = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_my_location)
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.lastLocation.addOnSuccessListener {
            it?.run {
                mLastUserPos = LatLng(latitude, longitude)
                Pref.putObj(Const.Pref.LAST_USER_POSITION, mLastUserPos)
                if (::mMap.isInitialized) {
                    mMap.addMarker(MarkerOptions().icon(marker).position(mLastUserPos!!))
                }
            }
        }
    }

    /**
     * Animate mapView so user position in center of the screen.
     * @param manually: When clicked at "my position" button.
     */
    private fun animateCameraMapView(manually: Boolean) {
        if (!::mMap.isInitialized) return
        if (viewModel.lastCamPos == null || manually) {
            mMap.animateCamera(
                CameraUpdateFactory
                    .newLatLngZoom(mLastUserPos ?: Const.hanoi, 14f), 800, null
            )
        } else {
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(viewModel.lastCamPos), 1, null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.lastCamPos = mMap.cameraPosition
    }
}


/**
 * Custom info window.
 */
class MyInfoWindow(private val context: Context) : GoogleMap.InfoWindowAdapter {
    override fun getInfoContents(marker: Marker): View {
        return inflate(marker)
    }

    override fun getInfoWindow(marker: Marker): View {
        return inflate(marker)
    }

    private fun inflate(marker: Marker): View {
        val view = context.layoutInflater().inflate(R.layout.my_info_window, null, false)
        view.tvTitle.text = marker.title
        val span = SpannableString(marker.snippet).apply {
            highlight("Địa chỉ:")
            highlight("Ngày cách ly:")
            highlight("Lộ trình:")
        }
        view.tvSnippet.text = span
        return view
    }
}

private fun SpannableString.highlight(s: String): SpannableString {
    if (!this.contains(s)) return this
    val start = this.indexOf(s)
    val end = start + s.length
    this.setSpan(ForegroundColorSpan(Color.BLACK), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    this.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}
