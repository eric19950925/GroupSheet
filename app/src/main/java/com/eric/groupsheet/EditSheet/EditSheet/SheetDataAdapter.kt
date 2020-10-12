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
import kotlinx.android.synthetic.main.item_name_list.view.item_MemberType
import kotlinx.android.synthetic.main.item_sheet_data.view.*

class SheetDataAdapter(var list: List<SheetItemClass>, private val sheetDataController: SheetDataController) :
    ListAdapter<SheetItemClass, BaseViewHolderWithController<SheetItemClass, SheetDataController>>(
        diffField
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolderWithController<SheetItemClass, SheetDataController> {
        return SheetDataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_sheet_data, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(
        holder: BaseViewHolderWithController<SheetItemClass, SheetDataController>,
        position: Int){
        val member_data: SheetItemClass = list[position]
        holder.onBind(member_data,sheetDataController)
    }
}

class SheetDataViewHolder(itemView: View) : BaseViewHolderWithController<SheetItemClass, SheetDataController>(itemView) {
    override fun onBind(data: SheetItemClass, controller: SheetDataController) {
        itemView.item_c1.text = controller.getRuleValue(0,data.mC1)
        itemView.item_c2.text = controller.getRuleValue(1,data.mC2)
        itemView.item_c3.text = controller.getRuleValue(2,data.mC3)
        itemView.item_c4.text = controller.getRuleValue(3,data.mC4)
        itemView.item_c5.text = controller.getRuleValue(4,data.mC5)
        itemView.item_total.text = controller.getMoney(data.mC1,data.mC2,data.mC3,data.mC4,data.mC5)
        itemView.item_submit.text = data.submitted.toString()
        itemView.item_left.text = (controller.getMoney(data.mC1,data.mC2,data.mC3,data.mC4,data.mC5).toInt() - data.submitted).toString()
        itemView.item_history.visibility = View.GONE
        itemView.item_MemberType.text =
            when(data.mType){
                1->"年長/男"
                2->"成人/男"
                3->"學生/男"
                4->"兒童/男"
                5->"年長/女"
                6->"成人/女"
                7->"學生/女"
                8->"兒童/女"
                else->"無分類"
            }
        itemView.setOnClickListener {
            controller.editMember(data.memberId)
        }

    }
}

interface SheetDataController: Controller {
    fun updateRvAfterDel(id: String)
    fun editMember(id: String)
    fun getRuleValue(title: Int, con: Int): String
    fun getMoney(
        mC1: Int,
        mC2: Int,
        mC3: Int,
        mC4: Int,
        mC5: Int
    ):String
}
val diffField = object  : DiffUtil.ItemCallback<SheetItemClass>() {
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