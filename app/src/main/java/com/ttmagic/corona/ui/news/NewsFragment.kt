package com.ttmagic.corona.ui.news

import android.content.Intent
import android.net.Uri
import com.base.mvvm.BaseAdapter
import com.base.mvvm.BaseFragment
import com.base.util.Bus
import com.ttmagic.corona.BR
import com.ttmagic.corona.MainActivity
import com.ttmagic.corona.R
import com.ttmagic.corona.databinding.FragmentNewsBinding
import com.ttmagic.corona.model.News
import com.ttmagic.corona.util.Const
import kotlinx.android.synthetic.main.fragment_news.*


class NewsFragment : BaseFragment<NewsVm, FragmentNewsBinding>(), BaseAdapter.DefaultClickListener {
    override fun brVariableId(): Int = BR.viewModel

    override fun layoutId(): Int = R.layout.fragment_news

    private val adapter = NewsAdapter(this)

    override fun initView() {
        rvNews.adapter = adapter
    }

    override fun <T> onItemClick(position: Int, item: T) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse((item as News).url)
        startActivity(i)
    }

    override fun observeData() {
        mViewModel.loading.observe {
            (activity as MainActivity?)?.showLoading(it)
        }

        mViewModel.listNews.observe {
            adapter.submitList(it as MutableList<News>)
        }

        Bus.get(Const.Bus.REFRESH).observe {
            mViewModel.getNews()
        }
    }
}