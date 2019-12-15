package com.yanzhikai.dialogtree.tree

import androidx.annotation.CallSuper

/**
 *
 * @author jacketyan
 * @date 2019/9/9
 */
/**
 * todo
 * 注释优化
 * 测试方式实现
 * 测试用例实现
 * 多线程支持
 * 流程优化
 */
open class DialogTreeNode<T>(private val dialogNode: DialogNode, private var data: T? = null) :
    DTNodeCallBack<T>
{
    companion object
    {
        private const val TAG = "DialogTreeNode"
    }

    internal val id: Long = DTIdGenerator.instance.generate()

    var positiveNode: DialogTreeNode<out Any>? = null
    var negativeNode: DialogTreeNode<out Any>? = null

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

    override fun onPreShow(data: T?) {

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

    override fun shouldShow(data: T?): Boolean {
        return true
    }

    open fun show()
    {
        dialogNode.dialog.show()
    }

    fun start(){

        onPreShow(data)

        if (shouldShow(data))
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

    override fun hashCode(): Int {
        return id.hashCode()
    }

}