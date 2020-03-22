package com.ttmagic.corona.ui.statsvn

import com.base.mvvm.BaseFragment
import com.base.util.addItemDividers
import com.base.util.show
import com.ttmagic.corona.BR
import com.ttmagic.corona.MainActivity
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
    }

    override fun observeData() {
        viewModel.isLoading.observe {
            (activity as MainActivity?)?.showLoading(it)
        }

        viewModel.stats.observe {
            if (it.isNotEmpty()) cvDetail.show()
            adapter.submitList(it as MutableList<StatsVn>)
        }
    }
}