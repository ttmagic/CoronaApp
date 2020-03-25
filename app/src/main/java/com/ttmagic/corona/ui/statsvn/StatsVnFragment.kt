package com.ttmagic.corona.ui.statsvn

import com.base.mvvm.BaseFragment
import com.base.util.Bus
import com.base.util.addItemDividers
import com.ttmagic.corona.BR
import com.ttmagic.corona.R
import com.ttmagic.corona.databinding.FragmentStatsVnBinding
import com.ttmagic.corona.model.Summary
import com.ttmagic.corona.ui.statsworld.addMarginTopEqualStatusBarHeight
import com.ttmagic.corona.util.Const
import kotlinx.android.synthetic.main.fragment_stats_vn.*

class StatsVnFragment :
    BaseFragment<StatsVnVm, FragmentStatsVnBinding>(R.layout.fragment_stats_vn) {
    override fun brVariableId(): Int = BR.viewModel
    override fun isFragmentScopeViewModel(): Boolean = false

    private val adapter = StatsVnAdapter()

    override fun initView(binding: FragmentStatsVnBinding) {
        rootView.addMarginTopEqualStatusBarHeight()
        rvStatistic.addItemDividers()
        rvStatistic.adapter = adapter
    }

    override fun observeData() {
        viewModel.stats.observe {
            adapter.submitList(it)
        }
        Bus.get(Const.Bus.SUMMARY_UPDATED).observe {
            viewModel.summary.postValue(it as Summary?)
        }
    }
}