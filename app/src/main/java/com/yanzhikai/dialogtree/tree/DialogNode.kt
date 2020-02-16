package com.yanzhikai.dialogtree.tree

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.util.SparseArray
import androidx.core.util.putAll
import androidx.core.util.set

/**
 * 用于生成DialogTreeNode转化器
 * 把用户使用的Dialog，转化为DialogTreeNode所需要的DialogNode
 */
class DialogNode private constructor(initialCallbackNum: Int) {
    lateinit var dialog: Dialog
    var dismissCallback: (() -> Unit)? = null
    var callBacks: SparseArray<DialogButtonCallback> = SparseArray(initialCallbackNum)

    companion object {
        /**
         * 通过传入AlertDialog.Builder来生成DialogNode
         * @param alertDialogBuilder Builder
         * @param positive String? 确认按钮文案，为空则是没有确认回调
         * @param negative String? 取消按钮文案，为空则是没有确认回调
         * @return DialogNode
         */
        @JvmStatic
        fun create(
            alertDialogBuilder: AlertDialog.Builder,
            positive: String?,
            negative: String?
        ): DialogNode {
            val dialogNode = DialogNode(2)

            positive?.let {
                val positiveCallback = DialogButtonCallback()
                alertDialogBuilder.setPositiveButton(positive) { _: DialogInterface, _: Int ->
                    positiveCallback.onCall()
                }
                dialogNode.callBacks[DTNodeCallBack.Type.POSITIVE] = positiveCallback
            }

            negative?.let {
                val negativeCallback = DialogButtonCallback()
                alertDialogBuilder.setNegativeButton(negative) { _: DialogInterface, _: Int ->
                    negativeCallback.onCall()
                }
                dialogNode.callBacks[DTNodeCallBack.Type.NEGATIVE] = negativeCallback
            }

            dialogNode.dialog = alertDialogBuilder.create()

            dialogNode.dialog.setOnDismissListener { dialogNode.dismissCallback?.invoke() }

            return dialogNode
        }


        /**
         * 通过传入AlertDialog来生成DialogNode
         * @param dialog dialog
         * @param positive String? 确认按钮文案，为空则是没有确认回调
         * @param negative String? 取消按钮文案，为空则是没有确认回调
         * @return DialogNode
         */
        @JvmStatic
        fun create(dialog: AlertDialog, positive: String?, negative: String?): DialogNode {
            val dialogNode = DialogNode(2)

            positive?.let {
                val positiveCallback = DialogButtonCallback()
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, positive) { _: DialogInterface, _: Int ->
                    positiveCallback.onCall()
                }
                dialogNode.callBacks[DTNodeCallBack.Type.POSITIVE] = positiveCallback
            }

            negative?.let {
                val negativeCallback = DialogButtonCallback()
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, negative) { _: DialogInterface, _: Int ->
                    negativeCallback.onCall()
                }
                dialogNode.callBacks[DTNodeCallBack.Type.NEGATIVE] = negativeCallback
            }

            dialog.setOnDismissListener {
                dialogNode.dismissCallback?.invoke()
            }

            dialogNode.dialog = dialog
            return dialogNode
        }

        /**
         * 自定义Dialog的转化方式
         * @param dialog Dialog
         * @param callBacks SparseArray<DialogButtonCallback> 需要以key，value方式传入callBack：
         *                  key作为识别callBacks的标识（具体规则是>= 0参考DTNodeCallBack.CallBackType），value则是实现DialogButtonCallback的方式
         * @return DialogNode
         */
        @JvmStatic
        fun create(dialog: Dialog, callBacks: SparseArray<DialogButtonCallback>): DialogNode {
            val dialogNode = DialogNode(callBacks.size() + 2)
            dialogNode.callBacks.putAll(callBacks)
            dialogNode.dialog = dialog
            return dialogNode
        }
    }

    class DialogButtonCallback(var callBack: (() -> Unit)? = null) {

        fun onCall() {
            callBack?.invoke()
        }
    }
}