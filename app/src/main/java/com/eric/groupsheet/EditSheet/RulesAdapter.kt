package com.eric.groupsheet.EditSheet

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.eric.groupsheet.R
import com.eric.groupsheet.base.BaseViewHolderWithController
import com.eric.groupsheet.base.Controller
import kotlinx.android.synthetic.main.item_edit_rule.view.*


class RulesAdapter(var ruleList: List<RuleClass>, private val ruleListController:RuleListController):
    ListAdapter<RuleClass,BaseViewHolderWithController<RuleClass,RuleListController>>(diffField3) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolderWithController<RuleClass, RuleListController> {
        return RulesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_edit_rule,parent,false))
    }

    override fun getItemCount(): Int = ruleList.size

    override fun onBindViewHolder(
        holder: BaseViewHolderWithController< RuleClass, RuleListController>,
        position: Int){
        val rule_data: RuleClass = ruleList[position]
        holder.onBind(rule_data,ruleListController)
    }
}
class RulesViewHolder(itemView: View) : BaseViewHolderWithController<RuleClass, RuleListController>(itemView) {
    override fun onBind(data: RuleClass, controller: RuleListController) {
        itemView.tv_cName.text = data.mRuleName
        itemView.tv_option1_name.text = data.mOption1Name
        itemView.tv_option1_value.text = data.mOption1Value
        itemView.tv_option2_name.text = data.mOption2Name
        itemView.tv_option2_value.text = data.mOption2Value
        itemView.tv_option3_name.text = data.mOption3Name
        itemView.tv_option3_value.text = data.mOption3Value
        itemView.tv_option4_name.text = data.mOption4Name
        itemView.tv_option4_value.text = data.mOption4Value
        itemView.tv_option5_name.text = data.mOption5Name
        itemView.tv_option5_value.text = data.mOption5Value
        itemView.setOnClickListener {
            controller.editRule(data.mRuleId)
        }
    }
}

interface RuleListController: Controller {
    fun updateRvAfterDel(id: String)
    fun editRule(id: String)
}
val diffField3 = object  : DiffUtil.ItemCallback<RuleClass>() {
    override fun areItemsTheSame(oldItem: RuleClass, newItem:RuleClass): Boolean {
        return oldItem.isItemSame(newItem)
    }

    override fun areContentsTheSame(oldItem: RuleClass, newItem: RuleClass): Boolean {
        return oldItem.isContentSameTo(newItem)
    }
    override fun getChangePayload(oldItem: RuleClass, newItem: RuleClass): Any? {
        return newItem
    }



}