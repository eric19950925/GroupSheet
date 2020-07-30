package com.eric.groupsheet.Sheet

import com.eric.groupsheet.R
import com.eric.groupsheet.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel

class Sheet:BaseFragment() {
    private val viewModel by viewModel<SheetViewModel>()
    override fun getLayoutRes(): Int =
        R.layout.fragment_sheet

    override fun initData() {
    }

    override fun initObserver() {
    }

    override fun initView() {
    }
    companion object{
        fun newInstance() = Sheet()
    }
}