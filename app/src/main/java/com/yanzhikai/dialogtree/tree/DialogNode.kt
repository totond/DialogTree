package com.yanzhikai.dialogtree.tree

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.util.SparseArray
import androidx.core.util.putAll
import androidx.core.util.set

class DialogNode private constructor(initialCallbackNum: Int) {
    lateinit var dialog: Dialog
    var dismissCallback: (() -> Unit)? = null
    var callBacks: SparseArray<DialogButtonCallback> = SparseArray(initialCallbackNum)

    companion object {
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
                dialogNode.callBacks[DTNodeCallBack.ButtonType.POSITIVE] = positiveCallback
            }

            negative?.let {
                val negativeCallback = DialogButtonCallback()
                alertDialogBuilder.setNegativeButton(negative) { _: DialogInterface, _: Int ->
                    negativeCallback.onCall()
                }
                dialogNode.callBacks[DTNodeCallBack.ButtonType.NEGATIVE] = negativeCallback
            }

            dialogNode.dialog = alertDialogBuilder.create()

            dialogNode.dialog.setOnDismissListener { dialogNode.dismissCallback?.invoke() }

            return dialogNode
        }


        @JvmStatic
        fun create(dialog: AlertDialog, positive: String?, negative: String?): DialogNode {
            val dialogNode = DialogNode(2)

            positive?.let {
                val positiveCallback = DialogButtonCallback()
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, positive) { _: DialogInterface, _: Int ->
                    positiveCallback.onCall()
                }
                dialogNode.callBacks[DTNodeCallBack.ButtonType.POSITIVE] = positiveCallback
            }

            negative?.let {
                val negativeCallback = DialogButtonCallback()
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, negative) { _: DialogInterface, _: Int ->
                    negativeCallback.onCall()
                }
                dialogNode.callBacks[DTNodeCallBack.ButtonType.NEGATIVE] = negativeCallback
            }

            dialog.setOnDismissListener {
                dialogNode.dismissCallback?.invoke()
            }

            dialogNode.dialog = dialog
            return dialogNode
        }

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