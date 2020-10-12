package com.eric.groupsheet.EditSheet.EditSheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.eric.groupsheet.MainHome.SheetItemClass
import com.eric.groupsheet.R
import com.eric.groupsheet.base.BaseViewHolderWithController
import com.eric.groupsheet.base.Controller
import kotlinx.android.synthetic.main.item_sheet_data_name.view.*

class SheetDataNameAdapter(var list: List<SheetItemClass>, private val sheetDataNameController: SheetDataNameController) :
    ListAdapter<SheetItemClass, BaseViewHolderWithController<SheetItemClass, SheetDataNameController>>(
        diffField2
    ),
    SheetDataNameController {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolderWithController<SheetItemClass, SheetDataNameController> {
        return SheetDataNameViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_sheet_data_name, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(
        holder: BaseViewHolderWithController<SheetItemClass, SheetDataNameController>,
        position: Int){
        val member_data: SheetItemClass = list[position]
        holder.onBind(member_data,sheetDataNameController)
    }
}

class SheetDataNameViewHolder(itemView: View) : BaseViewHolderWithController<SheetItemClass, SheetDataNameController>(itemView) {
    override fun onBind(data: SheetItemClass, controller: SheetDataNameController) {
        itemView.item_name.text = data.mName
    }
}

interface SheetDataNameController: Controller {

}
val diffField2 = object  : DiffUtil.ItemCallback<SheetItemClass>() {
    override fun areItemsTheSame(oldItem: SheetItemClass, newItem: SheetItemClass): Boolean {
        return oldItem.isItemSame(newItem)
    }

    override fun areContentsTheSame(oldItem: SheetItemClass, newItem: SheetItemClass): Boolean {
        return oldItem.isContentSameTo(newItem)
    }
    override fun getChangePayload(oldItem: SheetItemClass, newItem: SheetItemClass): Any? {
        return newItem
    }



}