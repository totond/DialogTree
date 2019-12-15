package com.yanzhikai.dialogtree

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface

class DialogNode{
    lateinit var dialog: Dialog
    var positiveCallback: (() -> Unit?)? = null
    var negativeCallback: (() -> Unit?)? = null

    companion object {
        @JvmStatic
        fun create(alertDialogBuilder: AlertDialog.Builder, positive: String?, negative: String?): DialogNode {
            val dialogNode = DialogNode()

            positive?.let {
                alertDialogBuilder.setPositiveButton(positive) { _: DialogInterface, _: Int ->
                    dialogNode.positiveCallback?.invoke()
                }
            }

            negative?.let {
                alertDialogBuilder.setNegativeButton(negative) { _: DialogInterface, _: Int ->
                    dialogNode.negativeCallback?.invoke()
                }
            }

            dialogNode.dialog = alertDialogBuilder.create()

            return dialogNode
        }
    }
}