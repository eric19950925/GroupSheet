package com.eric.groupsheet.base

import android.app.Dialog

interface DialogController {
    fun createDialog(
        condition: DialogCondition
    )
    fun dismiss()

    fun isDialogShowing(): Boolean
}
interface DialogListener {
    fun onDialogShow(dialog: Dialog)
    fun onDialogHide(dialog: Dialog)
}
data class DialogCondition(
    val title: String? = "",
    val message: String? = "",
    val right: Button? = null,
    val left: AbstractButton? = null,
    val top: AbstractButton? = null,
    val center: AbstractButton? = null,
    val down: AbstractButton? = null,
    val topImageResId: Int? = null,
    val centerImageResId: Int? = null,
    val centerMediaPath: String? = null,
    val isVideo: Boolean = false,
    val hasCancelButton: Boolean = false,
    val isAnimation: Boolean = false
)
abstract class AbstractButton {
    abstract val label: String
    abstract val action: (() -> Unit)?
}

data class Button(
    override val label: String,
    override val action: (() -> Unit)? = null
) : AbstractButton()

data class PositiveButton(
    override val label: String,
    override val action: (() -> Unit)? = null
) : AbstractButton()

data class NegativeButton(
    override val label: String,
    override val action: (() -> Unit)? = null
) : AbstractButton()