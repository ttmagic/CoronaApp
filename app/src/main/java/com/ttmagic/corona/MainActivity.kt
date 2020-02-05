package com.ttmagic.corona

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.base.mvvm.BaseActivity
import com.base.util.Bus
import com.base.util.gone
import com.base.util.show
import com.ttmagic.corona.util.Const
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    private lateinit var mNavController: NavController

    override fun navController(): NavController? = mNavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mNavController = findNavController(R.id.nav_host_fragment)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuNews -> mNavController.navigate(R.id.newsFragment)
            R.id.menuStatistics -> mNavController.navigate(R.id.statisticFragment)
            R.id.menuRefresh -> Bus.get(Const.Bus.REFRESH).postValue(true)
        }
        return true
    }

    fun showLoading(loading: Boolean) {
        if (loading) progressBar.show() else progressBar.gone()
    }
}
