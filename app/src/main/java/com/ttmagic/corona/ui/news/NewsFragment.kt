package com.ttmagic.corona.ui.news

import com.base.mvvm.BaseFragment
import com.ttmagic.corona.BR
import com.ttmagic.corona.R
import com.ttmagic.corona.databinding.FragmentNewsBinding

class NewsFragment : BaseFragment<NewsVm, FragmentNewsBinding>() {
    override fun brVariableId(): Int = BR.viewModel

    override fun layoutId(): Int = R.layout.fragment_news

    override fun initView() {

    }

}