package com.ttmagic.corona.ui.statsworld

import com.base.mvvm.BaseFragment
import com.base.util.addItemDividers
import com.ttmagic.corona.BR
import com.ttmagic.corona.R
import com.ttmagic.corona.databinding.FragmentStatsWorldBinding
import kotlinx.android.synthetic.main.fragment_stats_world.*


class StatsWorldFragment :
    BaseFragment<StatsWorldVm, FragmentStatsWorldBinding>(R.layout.fragment_stats_world) {
    override fun brVariableId(): Int = BR.viewModel
    override fun isFragmentScopeViewModel(): Boolean = false

    private val adapter = StatsWorldAdapter()

    override fun initView(binding: FragmentStatsWorldBinding) {
        rvStatistic.addItemDividers()
        rvStatistic.adapter = adapter
    }


    override fun observeData() {
        viewModel.stats.observe {
            adapter.submitList(it)
        }
    }
}