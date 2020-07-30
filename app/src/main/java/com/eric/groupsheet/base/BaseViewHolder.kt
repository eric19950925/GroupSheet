package com.eric.groupsheet.base

import android.content.Context
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolderWithController<data : Any, controller : Controller>(itemView: View) :
    BaseViewHolder<data>(itemView) {
    override fun onBind(data: data) {

    }

    abstract fun onBind(data: data, controller: controller)

    open fun onBind(data: data, payload: Any?, controller: controller) {}

    open fun onAttached(data: data, controller: controller) {}

    open fun onDetached(data: data, controller: controller) {}
}

abstract class BaseViewHolder<data : Any>(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    abstract fun onBind(data: data)

    fun getString(resId: Int): String {
        return context.getString(resId)
    }

    val context: Context = itemView.context
}

interface Controller

fun RecyclerView.ViewHolder.getColor(resId: Int): Int {
    return ResourcesCompat.getColor(itemView.resources, resId, null)
}