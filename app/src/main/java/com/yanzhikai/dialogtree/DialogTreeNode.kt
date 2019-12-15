package com.yanzhikai.dialogtree

import androidx.annotation.CallSuper

/**
 *
 *
 * @author jacketyan
 * @date 2019/9/9
 */
open class DialogTreeNode<T>(private val dialogNode: DialogNode, data: T? = null) : DTNodeCallBack
{
    companion object
    {
        private const val TAG = "DialogTreeNode"
    }

    internal val id: Long = DTIdGenerator.instance.generate()

    var positiveNode: DialogTreeNode<T>? = null
    var negativeNode: DialogTreeNode<T>? = null

    init {
        dialogNode.positiveCallback = {
            positiveNode?.show()
        }

        dialogNode.negativeCallback = {
            negativeNode?.show()
        }
    }


    override fun onShowCallback()
    {

    }

    override fun onPreShowCallBack() {

    }

    @CallSuper
    override fun onPositiveCallback()
    {
        positiveNode?.start()

        dialogNode.dialog.dismiss()
    }

    @CallSuper
    override fun onNegativeCallback()
    {
        negativeNode?.start()

        dialogNode.dialog.dismiss()
    }

    override fun onDismissCallback()
    {

    }

    override fun onShouldShowCallback(): Boolean
    {
        return true
    }

    open fun show()
    {
        dialogNode.dialog.show()
    }

    fun start(){

        onPreShowCallBack()

        if (onShouldShowCallback())
        {
            show()
        }
    }

    final override fun equals(other: Any?): Boolean
    {
        if (other is DialogTreeNode<*>)
        {
            return id == other.id
        }
        return false
    }

}