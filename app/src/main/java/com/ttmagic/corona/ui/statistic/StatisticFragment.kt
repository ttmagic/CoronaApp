package com.ttmagic.corona.ui.statistic

import com.base.mvvm.BaseFragment
import com.base.util.Bus
import com.base.util.addItemDividers
import com.base.util.show
import com.ttmagic.corona.BR
import com.ttmagic.corona.MainActivity
import com.ttmagic.corona.R
import com.ttmagic.corona.databinding.FragmentStatisticBinding
import com.ttmagic.corona.model.Province
import com.ttmagic.corona.util.Const
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
            if (it.isNotEmpty()) cvDetail.show()
            adapter.submitList(it as MutableList<Province>)
        }

        Bus.get(Const.Bus.REFRESH).observe {
            mViewModel.getListProvinces()
        }
    }
}