package com.yanzhikai.dialogtree.tree

import androidx.annotation.IntDef
import com.yanzhikai.dialogtree.tree.DTNodeCallBack.ButtonType.Companion.NEGATIVE
import com.yanzhikai.dialogtree.tree.DTNodeCallBack.ButtonType.Companion.POSITIVE
import io.reactivex.Observable

/**
 *
 *
 * @author jacketyan
 * @date 2019/9/9
 */
interface DTNodeCallBack<T> {
    fun onPositiveCall()

    fun onNegativeCall()

    fun onOtherButtonsCall(key: Int)

    fun onDismissCall()

    fun onPreShow(data: T?)

    fun getPreShowObservable(data: T?): Observable<Any>

    fun shouldShow(data: T?): Boolean

    fun onShowCallback()

    class ButtonType {
        companion object {
            const val POSITIVE = -1
            const val NEGATIVE = -2
        }
    }


    @IntDef(value = [POSITIVE, NEGATIVE])
    annotation class Type
}