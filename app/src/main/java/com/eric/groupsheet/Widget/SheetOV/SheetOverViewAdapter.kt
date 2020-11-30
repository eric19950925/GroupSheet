package com.eric.groupsheet.Widget.SheetOV

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.eric.groupsheet.MainHome.SheetClass
import com.eric.groupsheet.R
import com.eric.groupsheet.base.BaseViewHolderWithController
import com.eric.groupsheet.base.Controller
import com.eric.groupsheet.base.listenClick
import kotlinx.android.synthetic.main.item_sheet_over_view_list.view.*

class SheetOverViewAdapter(private val controller: SheetOVController) : ListAdapter<SheetClass, BaseViewHolderWithController<SheetClass, SheetOVController>>(
    diffField_sov
){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolderWithController<SheetClass, SheetOVController> {
        return SheetOViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_sheet_over_view_list,parent,false))    }

    override fun onBindViewHolder(
        holder: BaseViewHolderWithController<SheetClass, SheetOVController>,
        position: Int
    ) {
        holder.onBind(getItem(position),controller)
    }

}

class SheetOViewHolder(itemView: View) : BaseViewHolderWithController<SheetClass, SheetOVController>(itemView) {
    override fun onBind(data: SheetClass, controller: SheetOVController) {
        itemView.item_sov_name.text = data.mName
        itemView.item_sov_editDate.text = data.memberId
        itemView.cb_sov.isChecked = data.mCheck
        itemView.listenClick {
            controller.storeTargetSheet(data)

        }

    }

}

interface SheetOVController : Controller{
    fun storeTargetSheet(targetSheet : SheetClass)
}
val diffField_sov = object  : DiffUtil.ItemCallback<SheetClass>() {
    override fun areItemsTheSame(oldItem: SheetClass, newItem: SheetClass): Boolean {
        return oldItem.isItemSame(newItem)
    }

    override fun areContentsTheSame(oldItem: SheetClass, newItem: SheetClass): Boolean {
        return oldItem.isContentSameTo(newItem)
    }

}