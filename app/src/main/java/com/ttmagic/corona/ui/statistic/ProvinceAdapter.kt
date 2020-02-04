package com.ttmagic.corona.ui.statistic

import com.base.mvvm.BaseAdapter
import com.ttmagic.corona.BR
import com.ttmagic.corona.R
import com.ttmagic.corona.model.Province

class ProvinceAdapter : BaseAdapter<Province>() {
    override fun brVariableId(): Int = BR.item

    override fun layoutId(): Int = R.layout.item_statistic
}