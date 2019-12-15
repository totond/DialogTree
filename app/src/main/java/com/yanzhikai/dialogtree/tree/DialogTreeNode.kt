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
 * 检查是否覆盖原Dialog功能
 */
open class DialogTreeNode<T>(
    private val dialogNode: DialogNode,
    private var data: T? = null,
    private val alias: String? = null,
    private val testMode: Boolean = false
) :
    DTNodeCallBack<T> {
    companion object {
        private const val TAG = "DialogTreeNode"
    }

    internal val id: Long = DTIdGenerator.instance.generate()

    var positiveNode: DialogTreeNode<out Any>? = null
    var negativeNode: DialogTreeNode<out Any>? = null

    init {
        dialogNode.positiveCallback = {
            onPositiveCallback()
        }

        dialogNode.negativeCallback = {
            onNegativeCallback()
        }
    }


    override fun onShowCallback() {

    }

    override fun onPreShow(data: T?) {

    }

    @CallSuper
    override fun onPositiveCallback() {
        positiveNode?.start()

        dialogNode.dialog.dismiss()
    }

    @CallSuper
    override fun onNegativeCallback() {
        negativeNode?.start()

        dialogNode.dialog.dismiss()
    }

    override fun onDismissCallback() {

    }

    override fun shouldShow(data: T?): Boolean {
        return true
    }

    open fun show() {
        dialogNode.dialog.show()
    }

    fun start() {

        onPreShow(data)

        if (shouldShow(data)) {
            if (!testMode) {
                show()
            }
        }
    }

    fun testShow(positive: Boolean) {
        if (shouldShow(data)) {
            if (positive) {
                onPositiveCallback()
            } else {
                onNegativeCallback()
            }
        }
    }

    final override fun equals(other: Any?): Boolean {
        if (other is DialogTreeNode<*>) {
            return id == other.id
        }
        return false
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}