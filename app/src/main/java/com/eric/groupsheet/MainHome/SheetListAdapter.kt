package com.eric.groupsheet.MainHome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.eric.groupsheet.R
import com.eric.groupsheet.base.BaseViewHolderWithController
import com.eric.groupsheet.base.Controller
import kotlinx.android.synthetic.main.item_sheet_list.view.*

class SheetListAdapter(var list: List<SheetClass>, private val sheetListController: SheetListController) :
    ListAdapter<SheetClass, BaseViewHolderWithController<SheetClass, SheetListController>>(diffField) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolderWithController<SheetClass, SheetListController> {
        return SheetListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_sheet_list,parent,false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(
        holder: BaseViewHolderWithController<SheetClass, SheetListController>,
        position: Int){
        val member_data: SheetClass = list[position]
        holder.onBind(member_data,sheetListController)
    }
}

class SheetListViewHolder(itemView: View) : BaseViewHolderWithController<SheetClass, SheetListController>(itemView) {
    override fun onBind(data: SheetClass, controller: SheetListController) {
        itemView.item_SheetName.text = data.mName
        itemView.item_editDate.text = data.memberId

        itemView.setOnClickListener {

            controller.editMember(data.memberId)
        }

    }
}

interface SheetListController: Controller {
    fun updateRvAfterDel(id: String)
    fun editMember(id: String)
}
val diffField = object  : DiffUtil.ItemCallback<SheetClass>() {
    override fun areItemsTheSame(oldItem: SheetClass, newItem: SheetClass): Boolean {
        return oldItem.isItemSame(newItem)
    }

    override fun areContentsTheSame(oldItem: SheetClass, newItem: SheetClass): Boolean {
        return oldItem.isContentSameTo(newItem)
    }
    override fun getChangePayload(oldItem: SheetClass, newItem: SheetClass): Any? {
        return newItem
    }

}