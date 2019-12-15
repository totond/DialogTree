package com.yanzhikai.dialogtree.tree

/**
 *
 *
 * @author jacketyan
 * @date 2019/9/9
 */
interface DTNodeCallBack<T> {
    fun onPositiveCallback()

    fun onNegativeCallback()

    fun onDismissCallback()

    fun onPreShow(data: T?)

    fun shouldShow(data: T?): Boolean

    fun onShowCallback()
}