package com.ttmagic.corona.ui.statsvn

import androidx.lifecycle.Observer
import com.base.mvvm.BaseFragment
import com.base.util.addItemDividers
import com.ttmagic.corona.BR
import com.ttmagic.corona.R
import com.ttmagic.corona.databinding.FragmentStatsVnBinding
import com.ttmagic.corona.model.StatsVn
import kotlinx.android.synthetic.main.fragment_stats_vn.*

class StatsVnFragment :
    BaseFragment<StatsVnVm, FragmentStatsVnBinding>(R.layout.fragment_stats_vn) {
    override fun brVariableId(): Int = BR.viewModel
    override fun isFragmentScopeViewModel(): Boolean = false

    private val adapter = StatsVnAdapter()

    override fun initView(binding: FragmentStatsVnBinding) {
        rvStatistic.addItemDividers()
        rvStatistic.adapter = adapter
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.getStatsVn()
        }
    }

    override fun observeData() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            swipeRefreshLayout.setRefreshing(it)
        })

        viewModel.stats.observe {
            adapter.submitList(it)
            swipeRefreshLayout.setRefreshing(false)
        }
    }
}