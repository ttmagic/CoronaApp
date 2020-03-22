package com.ttmagic.corona.ui.statsvn

import com.base.mvvm.BaseAdapter
import com.ttmagic.corona.BR
import com.ttmagic.corona.R
import com.ttmagic.corona.model.StatsVn

class StatsVnAdapter : BaseAdapter<StatsVn>(R.layout.item_stats_vn) {
    override fun brVariableId(): Int = BR.item
}