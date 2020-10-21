package com.eric.groupsheet.MainHome

import android.graphics.Rect
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.textclassifier.TextClassifier.TYPE_EMAIL
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eric.groupsheet.R
import com.eric.groupsheet.base.BaseViewHolderWithController
import com.eric.groupsheet.base.Controller
import kotlinx.android.synthetic.main.item_sheet_list.view.*


open class SheetListAdapter(var list: List<SheetClass>, private val sheetListController: SheetListController) :
    ListAdapter<SheetClass, BaseViewHolderWithController<SheetClass, SheetListController>>(diffField) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolderWithController<SheetClass, SheetListController> {
        return SheetListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_sheet_list,parent,false))

//        when(viewType){
//            0 -> SheetListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_sheet_list,parent,false))
//            else ->SheetListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_sheet_list_side,parent,false))
//        }
    }
    override fun getItemViewType(position: Int): Int {
        return list[position].mType
    }
    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(
        holder: BaseViewHolderWithController<SheetClass, SheetListController>,
        position: Int){
        val member_data: SheetClass = list[position]
        holder.onBind(member_data,sheetListController)
    }
    open fun setViewType(position: Int,type: Int){
        list[position].mType = type
    }
}

class SheetListViewHolder(itemView: View) : BaseViewHolderWithController<SheetClass, SheetListController>(itemView) {
    override fun onBind(data: SheetClass, controller: SheetListController) {
        itemView.item_SheetName.text = data.mName
        itemView.item_editDate.text = data.memberId

        itemView.setOnClickListener {

            controller.editMember(data.memberId)
        }
//        controller.onItemChanged(adapterPosition)

    }
}


interface SheetListController: Controller {
    fun updateRvAfterDel(id: String)
    fun editMember(id: String)
    fun onItemChanged(p:Int)
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



class DefaultDecoration : RecyclerView.ItemDecoration() {
    var mItemConsumeX = 0
    private var sideVisibleWidth = 150
    fun getSideVisibleWidth() = sideVisibleWidth

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0

        val lp = view.layoutParams as RecyclerView.LayoutParams

        val itemWidth = parent.width - 2 * sideVisibleWidth

        mItemConsumeX = itemWidth

        if (lp.width != itemWidth) {
            lp.width = itemWidth
        }

//        val position = parent.getChildAdapterPosition(view)
//        val itemCount = parent.adapter?.itemCount ?: 0
//
//        val lp = view.layoutParams as RecyclerView.LayoutParams
//
//        val oneSideVisibleWith = 50
//        val itemWidth = parent.width - 2*oneSideVisibleWith
//
//        if (lp.width != itemWidth) {
//            lp.width = itemWidth
//        }
    }
}