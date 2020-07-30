package com.eric.groupsheet.Life

import android.content.DialogInterface
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.eric.groupsheet.MainActivity
import com.eric.groupsheet.MainHome.MainHomeViewModel
import com.eric.groupsheet.R
import com.eric.groupsheet.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_life.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class Life:BaseFragment() {
    override fun getLayoutRes(): Int = R.layout.fragment_life
    private val viewModel by sharedViewModel<LifeViewModel>()
    override fun initData() {
    }

    override fun initObserver() {
    }

    override fun initView() {
        imageView5.setOnClickListener {
            showIntro()
        }
        imageView6.setOnClickListener {
            val mainActivity: MainActivity = activity as MainActivity
            mainActivity.onBackPressed()
        }
        cv_key1.setOnClickListener {
            viewModel.KeyNumber.value = 1
            viewModel.toKey1()
        }
        cv_key2.setOnClickListener {
            viewModel.KeyNumber.value = 2
            viewModel.toKey1()
        }
        cv_key3.setOnClickListener {
            viewModel.KeyNumber.value = 3
            viewModel.toKey1()
        }
        cv_key4.setOnClickListener {
            viewModel.KeyNumber.value = 4
            viewModel.toKey1()
        }
    }

    private fun showIntro() {

        val LifeIntroView : View = getLayoutInflater().inflate(R.layout.dialog_life_intro,null)
        val tv_t1 : TextView = LifeIntroView.findViewById(R.id.tv_title1)
        val tv_t2 : TextView = LifeIntroView.findViewById(R.id.tv_title2)
        val tv_t3 : TextView = LifeIntroView.findViewById(R.id.tv_title3)
        val tv_t4 : TextView = LifeIntroView.findViewById(R.id.tv_title4)
        val tv_w1 : TextView = LifeIntroView.findViewById(R.id.tv_w1)
        val tv_w2 : TextView = LifeIntroView.findViewById(R.id.tv_w2)
        val tv_w3 : TextView = LifeIntroView.findViewById(R.id.tv_w3)
        val tv_w4 : TextView = LifeIntroView.findViewById(R.id.tv_w4)
        val tv_q1 : TextView = LifeIntroView.findViewById(R.id.tv_q1)
        val tv_q2 : TextView = LifeIntroView.findViewById(R.id.tv_q2)
        val tv_h1 : TextView = LifeIntroView.findViewById(R.id.tv_h1)
        val tv_h2 : TextView = LifeIntroView.findViewById(R.id.tv_h2)
        tv_t1.visibility = View.GONE
        tv_w1.text = getString(R.string.to_know)
        tv_h1.text = getString(R.string.to_know_h1)
        tv_t2.text = getString(R.string.title_the_reason_people_live)
        tv_w2.text = getString(R.string.msg_the_reason_people_live)
        tv_q1.text = getString(R.string.msg_the_reason_people_live_q1)
        tv_t3.text = getString(R.string.title_gods_plan)
        tv_w3.text = getString(R.string.msg_gods_plan)
        tv_h2.text = getString(R.string.msg_gods_plan_h2)
        tv_q2.text = getString(R.string.msg_gods_plan_q2)
        tv_t4.text = getString(R.string.title_four_keys)
        tv_w4.text = getString(R.string.msg_four_keys)
        val builder = activity?.let { it1 -> AlertDialog.Builder(it1) }
        builder?.setTitle("人生的奧秘 - 前言:")
        builder?.setView(LifeIntroView)
        builder?.setPositiveButton("確定", DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })
        builder?.show()
    }

    companion object{
        fun newInstance() = Life()
    }

}