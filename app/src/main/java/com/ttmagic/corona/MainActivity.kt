package com.ttmagic.corona

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.base.mvvm.BaseActivity
import com.base.util.Bus
import com.base.util.Logger
import com.base.util.gone
import com.base.util.show
import com.ttmagic.corona.ui.statsworld.StatsWorldVm
import com.ttmagic.corona.util.Const
import com.ttmagic.corona.util.GpsUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    private lateinit var mNavController: NavController

    override fun navController(): NavController? = mNavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mNavController = findNavController(R.id.nav_host_fragment)
        setTransparentStatusBar()

        botNav.setupWithNavController(mNavController)
        botNav.setOnNavigationItemReselectedListener {
            //Do nothing when reselected.
        }

        val statsWorldVm = ViewModelProvider(this).get(StatsWorldVm::class.java)
        //To trigger init method of StatsWorldVm
    }


    fun showLoading(loading: Boolean) {
        if (loading) progressBar.show() else progressBar.gone()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Logger.i("requestCode $requestCode resultCode $resultCode")
        if (requestCode == GpsUtils.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Bus.get(Const.Bus.GPS).postValue(true)  //Gps enabled.
        }
    }
}
