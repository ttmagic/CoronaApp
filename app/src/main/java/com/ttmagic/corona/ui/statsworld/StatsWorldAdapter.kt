package com.ttmagic.corona.ui.statsworld

import com.base.mvvm.BaseAdapter
import com.ttmagic.corona.BR
import com.ttmagic.corona.R
import com.ttmagic.corona.model.StatsWorld

class StatsWorldAdapter : BaseAdapter<StatsWorld>(R.layout.item_stats_world) {
    override fun brVariableId(): Int = BR.item
}