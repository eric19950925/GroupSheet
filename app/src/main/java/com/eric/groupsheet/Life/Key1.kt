package com.eric.groupsheet.Life

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.eric.groupsheet.R
import com.eric.groupsheet.base.BaseFragment
import com.eric.groupsheet.base.listenClick
import com.eric.groupsheet.base.observe
import kotlinx.android.synthetic.main.fragment_key.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

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
//        when(it){
//                1-> Toast.makeText(context,"1111111",Toast.LENGTH_SHORT).show()
//                2-> Toast.makeText(context,"2222222",Toast.LENGTH_SHORT).show()
//                3-> Toast.makeText(context,"3333333",Toast.LENGTH_SHORT).show()
//                4-> Toast.makeText(context,"4444444",Toast.LENGTH_SHORT).show()
//            }
        }
    }
    override fun initView() {
//        when(viewModel.KeyNumber.value?:0){
//            1-> ShowKey1()
//            2-> ShowKey2()
//            3-> ShowKey3()
//            4-> ShowKey4()
//            else -> Toast.makeText(context,"error",Toast.LENGTH_LONG).show()
//        }
    }

    @SuppressLint("InflateParams")
    private fun ShowKey4() {
        tv_title.text = getString(R.string.key4_title)
        tv_key_name.text = getString(R.string.key4_title_t1)
        tv_key_title.text = getString(R.string.key4_title_t2)
        val list = arrayListOf<View>()
        val mLayoutInflater = LayoutInflater.from(context)
        val view1 = mLayoutInflater.inflate(R.layout.view_key4_p1,null)
        val imageKey4v1_1 = view1.findViewById<ImageView>(R.id.imageView11)
        context?.let {
            Glide.with(it)
                .load(resources.getDrawable(R.drawable.three_part_human10))
                .apply(RequestOptions().override(3600, 1200))
                .placeholder(R.drawable.ball1)
                .into(imageKey4v1_1)
        }
        val view2 = mLayoutInflater.inflate(R.layout.view_key4_p2,null)
        val imageKey4v2_1 = view2.findViewById<ImageView>(R.id.img_Key4_v2_1)
        context?.let {
            Glide.with(it)
                .load(resources.getDrawable(R.drawable.three_part_human11))
                .apply(RequestOptions().override(3600, 1200))
                .placeholder(R.drawable.common_google_signin_btn_icon_dark_focused)
                .into(imageKey4v2_1)
        }
        val view3 = mLayoutInflater.inflate(R.layout.view_key4_p3,null)
        val imageKey4v3_1 = view3.findViewById<ImageView>(R.id.img_Key4_v3_1)
        context?.let {
            Glide.with(it)
                .load(resources.getDrawable(R.drawable.three_part_human12))
                .apply(RequestOptions().override(3600, 1200))
                .placeholder(R.drawable.common_google_signin_btn_icon_dark_focused)
                .into(imageKey4v3_1)
        }
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
        img_last.listenClick {
            viewModel.KeyNumber.value = 3
        }
        img_next.alpha = 0.2F
    }

    @SuppressLint("InflateParams")
    private fun ShowKey3() {
        img_next.alpha = 1F
        tv_title.text = getString(R.string.key3_title)
        tv_key_name.text = getString(R.string.key3_title_t1)
        tv_key_title.text = getString(R.string.key3_title_t2)
        val list = arrayListOf<View>()
        val mLayoutInflater = LayoutInflater.from(context)
        val view1 = mLayoutInflater.inflate(R.layout.view_key3_p1,null)
        val imageKey3v1_1 = view1.findViewById<ImageView>(R.id.imageView9)
        context?.let {
            Glide.with(it)
                .load(resources.getDrawable(R.drawable.three_part_human7))
                .apply(RequestOptions().override(3600, 1200))
                .placeholder(R.drawable.common_google_signin_btn_icon_dark_focused)
                .into(imageKey3v1_1)
        }
        val view2 = mLayoutInflater.inflate(R.layout.view_key3_p2,null)
        val imageKey3v2_1 = view2.findViewById<ImageView>(R.id.imageView10)
        context?.let {
            Glide.with(it)
                .load(resources.getDrawable(R.drawable.three_part_human9))
                .apply(RequestOptions().override(3600, 1200))
                .placeholder(R.drawable.common_google_signin_btn_icon_dark_focused)
                .into(imageKey3v2_1)
        }
        list.add(view1)
        list.add(view2)
        viewPager.adapter = ViewPagerAdapter(list)
        img_last.listenClick{
            viewModel.KeyNumber.value = 2
        }
        img_next.listenClick {
            viewModel.KeyNumber.value = 4
        }
    }

    @SuppressLint("InflateParams")
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
        val imageKey2v3_1 = view3.findViewById<ImageView>(R.id.imageView8)
        context?.let {
            Glide.with(it)
                .load(resources.getDrawable(R.drawable.three_part_human6))
                .apply(RequestOptions().override(3600, 1200))
                .placeholder(R.drawable.common_google_signin_btn_icon_dark_focused)
                .into(imageKey2v3_1)
        }
        val view4 = mLayoutInflater.inflate(R.layout.view_key2_p4,null)
        list.add(view1)
        list.add(view2)
        list.add(view3)
        list.add(view4)
        viewPager.adapter = ViewPagerAdapter(list)
        img_last.listenClick {
            viewModel.KeyNumber.value = 1
        }
        img_next.listenClick {
            viewModel.KeyNumber.value = 3
        }
    }


    @SuppressLint("InflateParams")
    private fun ShowKey1() {
        tv_title.text = getString(R.string.key1_title)
        tv_key_name.text = getString(R.string.key1_title_t1)
        tv_key_title.text = getString(R.string.key1_title_t2)
        val list = arrayListOf<View>()
        val mLayoutInflater = LayoutInflater.from(context)
        val versionAPI = Build.VERSION.SDK_INT
        val versionRelease = Build.VERSION.RELEASE
//        Toast.makeText(context,"API Version : $versionAPI\nVersion Release : $versionRelease",Toast.LENGTH_LONG).show()

        val view1 = mLayoutInflater.inflate(R.layout.view_key1_p1,viewPager,false)
        val view2 = mLayoutInflater.inflate(R.layout.view_key1_p2,viewPager,false)
        val view3 = mLayoutInflater.inflate(R.layout.view_key1_p3,viewPager,false)
        val imageKey1v3_1 = view3.findViewById<ImageView>(R.id.img_Key1_v3_1)
        context?.let {
            Glide.with(it)
                .load(resources.getDrawable(R.drawable.three_part_human2))
                .apply(RequestOptions().override(3600, 1200))
                .placeholder(R.drawable.common_google_signin_btn_icon_dark_focused)
                .into(imageKey1v3_1)
        }
        val imageKey1v3_2 = view3.findViewById<ImageView>(R.id.img_Key1_v3_2)
        context?.let {
            Glide.with(it)
                .load(resources.getDrawable(R.drawable.three_part_human4))
                .apply(RequestOptions().override(3600, 1200))
                .placeholder(R.drawable.common_google_signin_btn_icon_dark_focused)
                .into(imageKey1v3_2)
        }
        val view4 = mLayoutInflater.inflate(R.layout.view_key1_p4,viewPager,false)
        val imageKey1v4_1 = view4.findViewById<ImageView>(R.id.imageView7)
        context?.let {
            Glide.with(it)
                .load(resources.getDrawable(R.drawable.three_part_human5))
                .apply(RequestOptions().override(3600, 1200))
                .placeholder(R.drawable.common_google_signin_btn_icon_dark_focused)
                .into(imageKey1v4_1)
        }
        list.add(view1)
        list.add(view2)
        list.add(view3)
        list.add(view4)
        viewPager.adapter = ViewPagerAdapter(list)
        img_last.alpha = 0.2F
        img_next.listenClick {
            viewModel.KeyNumber.value = 2
        }
    }

    companion object{
        fun newInstance() = Key1()
    }

}