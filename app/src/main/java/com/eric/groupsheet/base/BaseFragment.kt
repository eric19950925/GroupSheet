package com.eric.groupsheet.base

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
    val TAG = "PermissionDemo"
    val RECORD_REQUEST_CODE = 101
    val RECORD_REQUEST_CODE_CAM = 102
    open fun setupPermissions() {
        val permission_write = activity?.let {
            ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (permission_write != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied")
            makeRequest()
        }

    }

    open fun setupPermissionsCam() {
        val permission_Cam = activity?.let { it1 ->
            ContextCompat.checkSelfPermission(
                it1,
                Manifest.permission.CAMERA)
        }
        if (permission_Cam != PackageManager.PERMISSION_GRANTED) {
            makeRequestCam()
        }

    }

    private fun makeRequest() {
        activity?.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                RECORD_REQUEST_CODE)
        }
    }

    private fun makeRequestCam() {
        activity?.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf(Manifest.permission.CAMERA),
                RECORD_REQUEST_CODE_CAM)
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user")
                } else {
                    Log.i(TAG, "Permission has been granted by user")
                }
            }
            RECORD_REQUEST_CODE_CAM -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user")
                } else {
                    Log.i(TAG, "Permission has been granted by user")
                }
            }
        }
    }
}