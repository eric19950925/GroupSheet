package com.eric.groupsheet.NameList

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.eric.groupsheet.R
import com.eric.groupsheet.base.BaseViewHolderWithController
import com.eric.groupsheet.base.Controller
import kotlinx.android.synthetic.main.item_name_list.view.*
//如何撰寫Adapter
class NameListAdapter(var list: List<MemberClass>, private val nameListController: NameListController) :
    ListAdapter<MemberClass, BaseViewHolderWithController<MemberClass, NameListController>>(diffField) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolderWithController<MemberClass, NameListController> {
        return NameListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_name_list,parent,false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(
        holder: BaseViewHolderWithController<MemberClass, NameListController>,
        position: Int){
        val member_data: MemberClass = list[position]
        holder.onBind(member_data,nameListController)
    }
}

class NameListViewHolder(itemView: View) : BaseViewHolderWithController<MemberClass, NameListController>(itemView) {
    override fun onBind(data: MemberClass, controller: NameListController) {
        itemView.item_MemberName.text = data.mName
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
        itemView.item_Edit.setOnClickListener {
            controller.editMember(data.memberId)
        }
        itemView.item_Delet.setOnClickListener {
            val builder = AlertDialog.Builder(itemView.context)
            builder.setTitle("確定要刪除嗎?")
            builder.setPositiveButton("確定", DialogInterface.OnClickListener { dialog, which ->
                controller.updateRvAfterDel(data.memberId)
                dialog.dismiss()
            })?.setNegativeButton("取消", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
            builder.show()
        }
    }
}

interface NameListController: Controller {
    fun updateRvAfterDel(id: String)
    fun editMember(id: String)
}
val diffField = object  : DiffUtil.ItemCallback<MemberClass>() {
    override fun areItemsTheSame(oldItem: MemberClass, newItem: MemberClass): Boolean {
        return oldItem.isItemSame(newItem)
    }

    override fun areContentsTheSame(oldItem: MemberClass, newItem: MemberClass): Boolean {
        return oldItem.isContentSameTo(newItem)
    }
    override fun getChangePayload(oldItem: MemberClass, newItem: MemberClass): Any? {
        return newItem
    }



}