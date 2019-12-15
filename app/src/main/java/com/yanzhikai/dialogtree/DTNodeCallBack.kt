package com.yanzhikai.dialogtree

/**
 *
 *
 * @author jacketyan
 * @date 2019/9/9
 */
interface DTNodeCallBack
{
    fun onPositiveCallback()

    fun onNegativeCallback()

    fun onDismissCallback()

    fun onPreShowCallBack()

    fun onShouldShowCallback(): Boolean

    fun onShowCallback()
}