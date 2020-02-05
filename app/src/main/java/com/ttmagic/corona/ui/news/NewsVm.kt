package com.ttmagic.corona.ui.news

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.base.mvvm.BaseViewModel
import com.base.mvvm.onSucceed
import com.ttmagic.corona.model.News
import com.ttmagic.corona.repo.network.network
import com.ttmagic.corona.util.Const

class NewsVm(app: Application) : BaseViewModel(app) {

    val listNews = MutableLiveData<List<News>>()


    init {
        getNews()
    }

    fun getNews() = coroutines {
        network().getNews(Const.newsQuery).onSucceed {
            listNews.value = it.data.topTrueNews
        }
    }
}