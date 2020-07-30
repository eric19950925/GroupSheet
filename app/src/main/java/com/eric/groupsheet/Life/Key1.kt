package com.eric.groupsheet.Life

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.eric.groupsheet.R
import com.eric.groupsheet.base.BaseFragment
import com.eric.groupsheet.base.observe
import kotlinx.android.synthetic.main.fragment_key.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class Key1:BaseFragment() {
    override fun getLayoutRes(): Int = R.layout.fragment_key
    private val viewModel by sharedViewModel<LifeViewModel>()

    override fun initData() {
    }

    override fun initObserver() {
        observe(viewModel.KeyNumber){
            when(it){
                1-> ShowKey1()
                2-> ShowKey2()
                3-> ShowKey3()
                4-> ShowKey4()
            }
        }
    }
    override fun initView() {
        when(viewModel.KeyNumber.value){
            1-> ShowKey1()
            2-> ShowKey2()
            3-> ShowKey3()
            4-> ShowKey4()
        }
    }

    private fun ShowKey4() {
        tv_title.text = getString(R.string.key4_title)
        tv_key_name.text = getString(R.string.key4_title_t1)
        tv_key_title.text = getString(R.string.key4_title_t2)
        val list = arrayListOf<View>()
        val mLayoutInflater = LayoutInflater.from(context)
        val view1 = mLayoutInflater.inflate(R.layout.view_key4_p1,null)
        val view2 = mLayoutInflater.inflate(R.layout.view_key4_p2,null)
        val view3 = mLayoutInflater.inflate(R.layout.view_key4_p3,null)
        val view4 = mLayoutInflater.inflate(R.layout.view_key4_p4,null)
        val view5 = mLayoutInflater.inflate(R.layout.view_key4_p5,null)
        val view6 = mLayoutInflater.inflate(R.layout.view_key4_p6,null)
        val view7 = mLayoutInflater.inflate(R.layout.view_key4_p7,null)
        val view8 = mLayoutInflater.inflate(R.layout.view_key4_p8,null)
        val view9 = mLayoutInflater.inflate(R.layout.view_key4_p9,null)
        list.add(view1)
        list.add(view2)
        list.add(view3)
        list.add(view4)
        list.add(view5)
        list.add(view6)
        list.add(view7)
        list.add(view8)
        list.add(view9)
        viewPager.adapter = ViewPagerAdapter(list)
        img_last.setOnClickListener {
            viewModel.KeyNumber.value = 3
        }
        img_next.alpha = 0.2F
    }

    private fun ShowKey3() {
        img_next.alpha = 1F
        tv_title.text = getString(R.string.key3_title)
        tv_key_name.text = getString(R.string.key3_title_t1)
        tv_key_title.text = getString(R.string.key3_title_t2)
        val list = arrayListOf<View>()
        val mLayoutInflater = LayoutInflater.from(context)
        val view1 = mLayoutInflater.inflate(R.layout.view_key3_p1,null)
        val view2 = mLayoutInflater.inflate(R.layout.view_key3_p2,null)
        list.add(view1)
        list.add(view2)
        viewPager.adapter = ViewPagerAdapter(list)
        img_last.setOnClickListener {
            viewModel.KeyNumber.value = 2
        }
        img_next.setOnClickListener {
            viewModel.KeyNumber.value = 4
        }
    }

    private fun ShowKey2() {
        img_last.alpha = 1F
        tv_title.text = getString(R.string.key2_title)
        tv_key_name.text = getString(R.string.key2_title_t1)
        tv_key_title.text = getString(R.string.key2_title_t2)
        val list = arrayListOf<View>()
        val mLayoutInflater = LayoutInflater.from(context)
        val view1 = mLayoutInflater.inflate(R.layout.view_key2_p1,null)
        val view2 = mLayoutInflater.inflate(R.layout.view_key2_p2,null)
        val view3 = mLayoutInflater.inflate(R.layout.view_key2_p3,null)
        val view4 = mLayoutInflater.inflate(R.layout.view_key2_p4,null)
        list.add(view1)
        list.add(view2)
        list.add(view3)
        list.add(view4)
        viewPager.adapter = ViewPagerAdapter(list)
        img_last.setOnClickListener {
            viewModel.KeyNumber.value = 1
        }
        img_next.setOnClickListener {
            viewModel.KeyNumber.value = 3
        }
    }


    private fun ShowKey1() {
        tv_title.text = getString(R.string.key1_title)
        tv_key_name.text = getString(R.string.key1_title_t1)
        tv_key_title.text = getString(R.string.key1_title_t2)
        val list = arrayListOf<View>()
        val mLayoutInflater = LayoutInflater.from(context)
        val view1 = mLayoutInflater.inflate(R.layout.view_key1_p1,null)
        val view2 = mLayoutInflater.inflate(R.layout.view_key1_p2,null)
        val view3 = mLayoutInflater.inflate(R.layout.view_key1_p3,null)
        val view4 = mLayoutInflater.inflate(R.layout.view_key1_p4,null)
        list.add(view1)
        list.add(view2)
        list.add(view3)
        list.add(view4)
        viewPager.adapter = ViewPagerAdapter(list)
        img_last.alpha = 0.2F
        img_next.setOnClickListener {
            viewModel.KeyNumber.value = 2
        }
    }

    companion object{
        fun newInstance() = Key1()
    }

}