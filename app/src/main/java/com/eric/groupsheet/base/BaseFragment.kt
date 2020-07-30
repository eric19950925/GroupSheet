package com.eric.groupsheet.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import com.eric.groupsheet.MVVM.Navigator
import com.eric.groupsheet.MainActivity
import org.koin.android.ext.android.inject

abstract class BaseFragment: Fragment() {
//    lateinit var dialogController: DialogController
    private val navigator by inject<Navigator>()
    override fun onAttach(context: Context) {
        super.onAttach(context)

//        if (context is DialogController) {
//            dialogController = context
//        }
    }
    protected abstract fun getLayoutRes(): Int
    private val lifecycleRegistry: LifecycleRegistry? by lazy { lifecycle as? LifecycleRegistry }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(getLayoutRes(), container, false)
        initData()
        return v
    }

    abstract fun initData()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
        performFragmentShow()
    }
    override fun onHiddenChanged(hidden: Boolean) {
        if (hidden) {
//            performFragmentHide()
        } else {
            performFragmentShow()
//            changeStatusBarIconColor()
        }
    }
    abstract fun initObserver()

    abstract fun initView()
    protected open fun onFragmentShow() {
    }
    private fun performFragmentShow() {
        lifecycleRegistry?.handleLifecycleEvent(Lifecycle.Event.ON_START)
        onFragmentShow()
    }
    open fun onBackPressed(): Boolean {
        return false
}


    fun removeFragment(fragment: Fragment){
        val mainActivity: MainActivity = activity as MainActivity
        mainActivity.removeFragment(fragment)
//        childFragmentManager.beginTransaction().remove(fragment).commit()
    }
}