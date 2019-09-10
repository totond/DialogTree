package com.yanzhikai.dialogtree

import android.app.Dialog
import androidx.annotation.CallSuper
import io.reactivex.Flowable
import java.util.concurrent.Callable

/**
 *
 *
 * @author jacketyan
 * @date 2019/9/9
 */
open class DialogTreeNode<T>(val dialog: Dialog) : DTNodeCallBack
{
    companion object
    {
        private const val TAG = "DialogTreeNode"
    }

    internal val id: Long = DTIdGenerator.instance.generate()

    var positiveNode: DialogTreeNode<T>? = null
    var negativeNode: DialogTreeNode<T>? = null


    override fun onShowCallback()
    {

    }

    @CallSuper
    override fun onPositiveCallback()
    {
        handleNextNode(positiveNode)
    }

    @CallSuper
    override fun onNegativeCallback()
    {
        handleNextNode(negativeNode)
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
        dialog.show()
    }

    fun handleNextNode(node: DialogTreeNode<T>?)
    {


        if (node?.onShouldShowCallback() == true)
        {
            node.show()
        }
        dialog.dismiss()
        return
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