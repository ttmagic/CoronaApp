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
import com.base.mvvm.BaseFragment
import com.base.util.Bus
import com.base.util.Pref
import com.base.util.layoutInflater
import com.base.util.onClick
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
import com.ttmagic.corona.util.Const
import com.ttmagic.corona.util.GpsUtils
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.my_info_window.view.*

val f0Visited by lazy { BitmapDescriptorFactory.fromResource(R.drawable.ic_place) }
val f0 by lazy { BitmapDescriptorFactory.fromResource(R.drawable.ic_f0) }
val f1 by lazy { BitmapDescriptorFactory.fromResource(R.drawable.ic_f1) }
val f2 by lazy { BitmapDescriptorFactory.fromResource(R.drawable.ic_f2) }
val f3 by lazy { BitmapDescriptorFactory.fromResource(R.drawable.ic_f3) }

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
        updateMapMarkers(viewModel.listMarkers.value)
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

        viewModel.listMarkers.observe {
            updateMapMarkers(it)
        }
    }

    private fun updateMapMarkers(markers: List<MarkerOptions>?) {
        markers?.let {
            if (::mMap.isInitialized) {
                mMap.clear()
                addCurrPosMarker()
                it.forEach { item -> mMap.addMarker(item) }
            }
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
