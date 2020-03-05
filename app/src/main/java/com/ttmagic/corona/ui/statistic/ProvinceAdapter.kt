package com.ttmagic.corona.ui.statistic

import com.base.mvvm.BaseAdapter
import com.ttmagic.corona.BR
import com.ttmagic.corona.R
import com.ttmagic.corona.model.Province

class ProvinceAdapter : BaseAdapter<Province>(R.layout.item_statistic) {
    override fun brVariableId(): Int = BR.item
}