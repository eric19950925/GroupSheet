package com.eric.groupsheet.Widget.GSVerse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.eric.groupsheet.MainHome.SheetClass
import com.eric.groupsheet.R
import com.eric.groupsheet.Widget.SheetOV.SheetOVController
import com.eric.groupsheet.Widget.SheetOV.SheetOViewHolder
import com.eric.groupsheet.Widget.SheetOV.diffField_sov
import com.eric.groupsheet.base.BaseViewHolderWithController
import com.eric.groupsheet.base.Controller
import com.eric.groupsheet.base.listenClick
import kotlinx.android.synthetic.main.item_gsv_style_list.view.*
import kotlinx.android.synthetic.main.item_sheet_over_view_list.view.*

class GSVerseAdapter (private val controller: GSVController) : ListAdapter<StyleClass, BaseViewHolderWithController<StyleClass, GSVController>>(
    diffField_gsv
){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolderWithController<StyleClass, GSVController> {
        return GSViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_gsv_style_list,parent,false))    }

    override fun onBindViewHolder(
        holder: BaseViewHolderWithController<StyleClass, GSVController>,
        position: Int
    ) {
        holder.onBind(getItem(position),controller)
    }

}
class GSViewHolder(itemView: View) : BaseViewHolderWithController<StyleClass, GSVController>(itemView) {
    override fun onBind(data: StyleClass, controller: GSVController) {
        itemView.item_gsv_name.text = data.styleName
        itemView.item_gsv_hint.text = data.styleHint
        itemView.item_gsv_img.setImageResource(data.styleImgId)
        itemView.cb_gsv.isChecked = data.styleSelect
        itemView.listenClick {
            controller.storeStyle(data)

        }

    }

}

interface GSVController : Controller {
    fun storeStyle(style : StyleClass)
}
val diffField_gsv = object  : DiffUtil.ItemCallback<StyleClass>() {
    override fun areItemsTheSame(oldItem: StyleClass, newItem: StyleClass): Boolean {
        return oldItem.isItemSame(newItem)
    }

    override fun areContentsTheSame(oldItem: StyleClass, newItem: StyleClass): Boolean {
        return oldItem.isContentSameTo(newItem)
    }

}