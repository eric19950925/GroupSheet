package com.eric.groupsheet.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.view.Window
import androidx.core.content.res.ResourcesCompat
import com.eric.groupsheet.R
import kotlinx.android.synthetic.main.my_custom_dialog.*

object CustomDialog {
    fun showDialog(activity: Activity, condition: DialogCondition): Dialog {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.my_custom_dialog)
        activity.getScreen { width, _ ->
            dialog.clDialogContain.layoutParams = dialog.clDialogContain.layoutParams.apply {
                this.width = width * 8 / 10
            }
        }

        dialog.tvTitle.text = condition.title
        dialog.tvTitle.setTextColor(R.color.colorPrimaryDark)
        dialog.tvMessage.text = condition.message
        dialog.tvMessage.setTextColor(R.color.colorPrimaryDark)

        if (condition.topImageResId == null && !condition.hasCancelButton) {
            dialog.clTopImg.hide()
        } else {
            dialog.clTopImg.show()
            if (condition.topImageResId != null) {
                dialog.imgTop.setImageResource(condition.topImageResId)
                dialog.imgTop.show()
            } else {
                dialog.imgTop.hide()
            }
            if (condition.hasCancelButton) {
                dialog.imgCancel.show()
                dialog.imgCancel.setOnClickListener{
                    dialog.dismiss()
                }
            } else {
                dialog.imgCancel.hide()
            }
        }

        if (condition.centerMediaPath == null && condition.centerImageResId == null) {
            dialog.clCenter.hide()
        } else {
            dialog.clCenter.show()
            if (condition.centerImageResId != null || !condition.isVideo) {
                dialog.imgCenter.show()
//                if (condition.centerImageResId != null) {
//                    if (condition.isAnimation) {
//                        Glide.with(activity)
//                            .load(condition.centerImageResId)
//                            .into(dialog.imgCenter)
//                    } else {
//                        dialog.imgCenter.setImageResource(condition.centerImageResId)
//                    }
//                } else if (condition.centerMediaPath != null) {
//                    Glide.with(activity)
//                        .load(condition.centerMediaPath)
//                        .into(dialog.imgCenter)
//                }
            } else {
                dialog.imgCenter.hide()
            }

            if (condition.isVideo) {
                dialog.videoCenter.setVideoPath(condition.centerMediaPath)
                dialog.videoCenter.start()
            } else {
                dialog.videoCenter.hide()
            }
        }

        dialog.llHorizontalButton.show(condition.left != null || condition.right != null)

        dialog.llVerticalButton.show(condition.top != null || condition.center != null || condition.down != null)

        if (condition.top == null) {
            dialog.btnTop.hide()
        } else {
            with(dialog.btnTop) {
                setupButton(condition.top, activity, dialog)
            }
        }

        if (condition.center == null) {
            dialog.btnCenter.hide()
        } else {
            with(dialog.btnCenter) {
                setupButton(condition.center, activity, dialog)
            }
        }

        if (condition.down == null) {
            dialog.btnBottom.hide()
        } else {
            with(dialog.btnBottom) {
                setupButton(condition.down, activity, dialog)
            }
        }

        if (condition.left == null) {
            dialog.btnLeft.hide()
        } else {
            with(dialog.btnLeft) {
                setupButton(condition.left, activity, dialog)
            }
        }

        if (condition.right == null) {
            dialog.btnRight.hide()
        } else {
            with(dialog.btnRight) {
                setupButton(condition.right, activity, dialog)
            }
        }

        dialog.show()
        return dialog

    }
    @SuppressLint("ResourceAsColor")
    private fun android.widget.Button.setupButton(
        it: AbstractButton,
        activity: Activity,
        dialog: Dialog
    ) {
        when (it) {
            is PositiveButton -> {
                background = ResourcesCompat.getDrawable(
                    activity.resources,
                    R.color.colorPrimary,
                    null
                )
                setTextColor(Color.WHITE)
            }
//            is NeutralButton -> {
//                background = ResourcesCompat.getDrawable(
//                    activity.resources,
//                    R.drawable.neutral_button_background,
//                    null
//                )
//                setTextColor(
//                    ResourcesCompat.getColor(
//                        activity.resources,
//                        R.color.dlink_blue,
//                        null
//                    )
//                )
//            }
            is NegativeButton -> {
                background = ResourcesCompat.getDrawable(
                    activity.resources,
                    R.color.colorPrimary,
                    null
                )
                setTextColor(Color.WHITE)
            }
            else -> {
                background = ResourcesCompat.getDrawable(
                    activity.resources,
                    R.color.colorPrimary,
                    null
                )
                setTextColor(R.color.colorPrimaryDark)
            }
        }
        text = it.label

        setOnClickListener { _ ->
            dialog.dismiss()
            it.action?.invoke()
        }
    }
}