package com.eric.groupsheet.MainHome

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class WheelAnimationScrollerListener(private val itemWith: Int) : RecyclerView.OnScrollListener(){

    private var scrolledWidth = 0
    private var mPosition = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        scrolledWidth += dx

        val offset = scrolledWidth.toFloat() / itemWith.toFloat()

        val position = offset.toInt()

        val percent = offset - position
//        Log.d("TAG",dx.toString())
        setFadeView(recyclerView,position,percent)

    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
    }

    fun setFadeView(recyclerView: RecyclerView, position: Int, percent: Float) {
        // 中間頁面
        val mCurView = recyclerView.layoutManager!!.findViewByPosition(position)
        // 右邊頁面
        val mRightView = recyclerView.layoutManager!!.findViewByPosition(position + 1)
        // 左邊頁面
        val mLeftView = recyclerView.layoutManager!!.findViewByPosition(position - 1)
        // 右右邊頁面，再向右滑的時候會出現
        val mRRView = recyclerView.layoutManager!!.findViewByPosition(position + 2)

        mRightView?.alpha = 0.5f
        mLeftView?.alpha = 0.5f
        mRRView?.alpha = 1f
        mCurView?.alpha = 1f
    }

    fun updatePosition(currentPosition: Int) {
        scrolledWidth = currentPosition*itemWith
    }


}