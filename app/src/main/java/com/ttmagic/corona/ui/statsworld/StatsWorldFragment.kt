package com.ttmagic.corona.ui.statsworld

import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.LinearLayout
import com.base.mvvm.BaseFragment
import com.base.util.addItemDividers
import com.base.util.getStatusBarHeight
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
        rootView.addMarginTopEqualStatusBarHeight()
        rvStatistic.addItemDividers()
        rvStatistic.adapter = adapter
    }


    override fun observeData() {
        viewModel.stats.observe {
            adapter.submitList(it)
        }
    }
}

fun View?.addMarginTopEqualStatusBarHeight() {
    if (this == null) return
    setTag("TAG_OFFSET")
    val keyOffset = -123
    val haveSetOffset: Any? = getTag(keyOffset)
    if (haveSetOffset != null && haveSetOffset as Boolean) return
    val layoutParams = layoutParams as MarginLayoutParams
    layoutParams.setMargins(
        layoutParams.leftMargin,
        layoutParams.topMargin + context.getStatusBarHeight(),
        layoutParams.rightMargin,
        layoutParams.bottomMargin
    )
    setTag(keyOffset, true)
}