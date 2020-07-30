package com.eric.groupsheet

import com.eric.groupsheet.base.BaseFragment

class Setting:BaseFragment() {
    override fun getLayoutRes(): Int = R.layout.fragment_setting

    override fun initData() {
    }

    override fun initObserver() {
    }

    override fun initView() {
    }
    companion object{
        fun newInstance() = Setting()
    }
}