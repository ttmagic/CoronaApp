package com.ttmagic.corona.ui.news

import com.base.mvvm.BaseAdapter
import com.ttmagic.corona.BR
import com.ttmagic.corona.R
import com.ttmagic.corona.model.News

class NewsAdapter(listener: DefaultClickListener) : BaseAdapter<News>(listener) {
    override fun brVariableId(): Int = BR.item

    override fun layoutId(): Int = R.layout.item_news

}