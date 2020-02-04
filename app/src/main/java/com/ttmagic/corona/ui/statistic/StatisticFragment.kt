package com.ttmagic.corona.ui.statistic

import com.base.mvvm.BaseFragment
import com.base.util.addItemDividers
import com.ttmagic.corona.BR
import com.ttmagic.corona.MainActivity
import com.ttmagic.corona.R
import com.ttmagic.corona.databinding.FragmentStatisticBinding
import com.ttmagic.corona.model.Province
import kotlinx.android.synthetic.main.fragment_statistic.*

class StatisticFragment : BaseFragment<StatisticVm, FragmentStatisticBinding>() {

    override fun brVariableId(): Int = BR.viewModel

    override fun layoutId(): Int = R.layout.fragment_statistic

    private val adapter = ProvinceAdapter()

    override fun initView() {
        rvStatistic.addItemDividers()
        rvStatistic.adapter = adapter
    }

    override fun observeData() {
        mViewModel.loading.observe{
            (activity as MainActivity?)?.showLoading(it)
        }

        mViewModel.listProvinces.observe {
            adapter.submitList(it as MutableList<Province>)
        }
    }
}